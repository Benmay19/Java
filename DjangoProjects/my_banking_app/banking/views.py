import datetime
import json
from decimal import *
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from django.core.paginator import Paginator
from django.db import IntegrityError
from django.http import JsonResponse
from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render
from django.urls import reverse
from django.views.decorators.csrf import csrf_exempt

from .models import User, Financial, Recurring

# Create your views here.

@login_required(login_url='login')
def index(request):
    user = User.objects.get(username=request.user.username)
    balances = Financial.objects.get(user=request.user)
    return render(request, "banking/index.html", {
        "user": user,
        "balances": balances
    })

def login_view(request):
    if request.method == "POST":

        # Attempt to sign user in
        username = request.POST["username"]
        password = request.POST["password"]
        user = authenticate(request, username=username, password=password)

        # Check if authentication successful
        if user is not None:
            login(request, user)
            return HttpResponseRedirect(reverse("index"))
        else:
            return render(request, "banking/login.html", {
                "message": "Invalid username and/or password."
            })
    else:
        return render(request, "banking/login.html")


def logout_view(request):
    logout(request)
    return HttpResponseRedirect(reverse("index"))


def register(request):
    if request.method == "POST":
        username = request.POST["username"]
        email = request.POST["email"]

        # Ensure password matches confirmation
        password = request.POST["password"]
        confirmation = request.POST["confirmation"]
        if password != confirmation:
            return render(request, "banking/register.html", {
                "message": "Passwords must match."
            })

        # Attempt to create new user
        try:
            user = User.objects.create_user(username, email, password)
            user.save()
        except IntegrityError:
            return render(request, "banking/register.html", {
                "message": "Username already taken."
            })
        login(request, user)
        create_financial = Financial(user=user)
        create_financial.save()
        return HttpResponseRedirect(reverse("index"))
    else:
        return render(request, "banking/register.html")

@login_required(login_url='login')
def balances(request):
    balances = Financial.objects.get(user=request.user)
    return render(request, "banking/balances.html", {
        "balances": balances
    })

@csrf_exempt
@login_required(login_url='login')
def deposit(request):
    if request.method == "PUT":
        data = json.loads(request.body)
        account = data.get("account")
        amount = data.get("amount")
        user = request.user
        financial = Financial.objects.get(user=user)
        if account == "checking":
            financial.checking += Decimal(amount)
        else:
            financial.savings += Decimal(amount)
        financial.save()
        return JsonResponse({"message": "Account balance updated successfully."}, status=201)
    else:
        return JsonResponse({"error": "PUT request required."}, status=400) 

@csrf_exempt
@login_required(login_url='login')
def withdrawl(request):
    if request.method == "PUT":
        data = json.loads(request.body)
        account = data.get("account")
        amount = data.get("amount")
        user = request.user
        financial = Financial.objects.get(user=user)
        if account == "checking":
            financial.checking -= Decimal(amount)
        else:
            financial.savings -= Decimal(amount)
        financial.save()
        return JsonResponse({"message": "Account balance updated successfully."}, status=201)
    else:
        return JsonResponse({"error": "PUT request required."}, status=400)

@csrf_exempt
@login_required(login_url='login')
def transfer(request):
    if request.method == "PUT":
        data = json.loads(request.body)
        account = data.get("account")
        amount = data.get("amount")
        user = request.user
        financial = Financial.objects.get(user=user)
        if account == "ctos":
            financial.checking -= Decimal(amount)
            financial.savings += Decimal(amount)
        else:
            financial.savings -= Decimal(amount)
            financial.checking += Decimal(amount)
        financial.save()
        return JsonResponse({"message": "Account balance updated successfully."}, status=201)
    else:
        return JsonResponse({"error": "PUT request required."}, status=400)

def details(request):
    balances = Financial.objects.get(user=request.user)
    recurring = Recurring.objects.filter(user=request.user).all().order_by("day")
    date = datetime.date.today()
    month_year = date.strftime("%B %Y")
    day = date.strftime("%d")
    new_checking = balances.checking
    new_savings = balances.savings
    updated_checking = []
    updated_savings = []
    crange = []
    srange = []
    cday = []
    sday = []
    cname = []
    sname = []
    camount = []
    samount = []
    for item in recurring:
        if item.account == "checking" and item.day > int(day):
            if item.bill:
                new_checking -= item.amount
                updated_checking.append(new_checking)
                cday.append(item.day)
                cname.append(item.name)
                camount.append(item.amount)
            else:
                new_checking += item.amount
                updated_checking.append(new_checking)
                cday.append(item.day)
                cname.append(item.name)
                camount.append(item.amount)
        else:
            if item.bill:
                new_savings -= item.amount
                updated_savings.append(new_savings)
                sday.append(item.day)
                sname.append(item.name)
                samount.append(item.amount)
            else:
                new_savings += item.amount
                updated_savings.append(new_savings)
                sday.append(item.day)
                sname.append(item.name)
                samount.append(item.amount)
    for i in range(len(cname)):
        crange.append(i)
    for i in range(len(sname)):
        srange.append(i)
    return render(request, "banking/details.html", {
        "balances": balances,
        "month_year": month_year,
        "updated_checking": updated_checking,
        "updated_savings": updated_savings,
        "crange": crange,
        "srange": srange,
        "cday": cday,
        "cname": cname,
        "camount": camount,
        "sday": sday,
        "sname": sname,
        "samount": samount
    })

@csrf_exempt
@login_required(login_url='login')
def recurring(request):
    user_rec = Recurring.objects.filter(user=request.user).all().order_by("day")
    if len(user_rec) > 0:
        paginator = Paginator(user_rec, 10)
        num = request.GET.get("page")
        recurring = paginator.get_page(num)
        return render(request, "banking/recurring.html", {
            "recurring": recurring
        })
    else:
        return render(request, "banking/recurring.html", {
            "message": "No Recurring Bills or Payments Added."
        })

@csrf_exempt
@login_required(login_url='login')
def add(request):
    if request.method == "POST":
        data = json.loads(request.body)
        account = data.get("account")
        rtype = data.get("rtype")
        name = data.get("name")
        amount = data.get("amount")
        day = data.get("day")
        if rtype == "bill":
            bill = True
        else:
            bill = False
        recurring = Recurring(user=request.user, account=account, bill=bill, name=name, amount=amount, day=day)
        recurring.save()
        return JsonResponse({"message": "Recurring bill/payment created successfully."}, status=201)
    else:
        return JsonResponse({"error": "POST request required."}, status=400)

def remove(request, item_id):
    delete = Recurring.objects.get(id=item_id)
    if request.method == "POST":
        delete.delete()
        return HttpResponseRedirect(reverse("recurring"))