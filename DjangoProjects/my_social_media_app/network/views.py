import json
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from django.core.paginator import Paginator
from django.db import IntegrityError
from django.http import JsonResponse
from django.http import HttpResponse, HttpResponseRedirect
from django.shortcuts import render
from django.urls import reverse
from django.views.decorators.csrf import csrf_exempt

from .models import User, Post, Profile


def index(request):
    allposts = Post.objects.all().order_by("-timestamp")
    paginator = Paginator(allposts, 10)
    num = request.GET.get("page")
    page = paginator.get_page(num)

    return render(request, "network/index.html", {
        "page": page,
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
            return render(request, "network/login.html", {
                "message": "Invalid username and/or password."
            })
    else:
        return render(request, "network/login.html")


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
            return render(request, "network/register.html", {
                "message": "Passwords must match."
            })

        # Attempt to create new user
        try:
            user = User.objects.create_user(username, email, password)
            user.save()
        except IntegrityError:
            return render(request, "network/register.html", {
                "message": "Username already taken."
            })
        login(request, user)
        new_profile = Profile(username=user)
        new_profile.save()
        return HttpResponseRedirect(reverse("index"))
    else:
        return render(request, "network/register.html")

@csrf_exempt
@login_required(login_url='login')
def compose(request):
    if request.method != "POST":
        return JsonResponse({"error": "POST request required."}, status=400)
    
    data = json.loads(request.body)
    content = data.get("content", "")

    post = Post(
        username=request.user,
        content=content 
    )
    post.save()

    return JsonResponse({"message": "Post published successfully."}, status=201)

def get_timestamp(obj):
    return obj["timestamp"]

@login_required(login_url='login')
def following(request):
    user = Profile.objects.get(username=request.user)
    users_i_follow = user.following.all()
    posts_i_follow = []
    for users in users_i_follow:
        user_posts = Post.objects.all().filter(username=users)
        for posts in user_posts:
            posts_i_follow.append(posts)
    posts_i_follow.sort(key=lambda posts: posts.timestamp, reverse=True)
    paginator = Paginator(posts_i_follow, 10)
    num = request.GET.get("page")
    page = paginator.get_page(num)

    return render(request, "network/following.html", {
        "page": page
    })

@login_required(login_url='login')  
def profile(request, username):
    user = User.objects.get(username=username)
    profile = Profile.objects.get(username=user)
    user_posts = Post.objects.filter(username=username).all().order_by("-timestamp")
    paginator = Paginator(user_posts, 10)
    num = request.GET.get("page")
    page = paginator.get_page(num)
    return render(request, "network/profile.html", {
        "profile": profile,
        "page": page,
        "user": user
    })

@csrf_exempt
@login_required(login_url='login')
def follow(request):
    if request.method == "PUT":
        data = json.loads(request.body)
        user_id = data.get("user_to_follow")
        user_to_follow = User.objects.get(id=user_id)
        username = request.user
        follower = Profile.objects.get(username=user_to_follow)
        following = Profile.objects.get(username=username)
        if username in follower.followers.all():
            follower.followers.remove(username)
            following.following.remove(user_to_follow)
        else:
            follower.followers.add(username)
            following.following.add(user_to_follow)
        follower.save()
        following.save()
        return JsonResponse({"message": "Follow status updated successfully."}, status=201)
    else:
        return JsonResponse({"error": "PUT request required."}, status=400)

@csrf_exempt
@login_required(login_url='login')
def toggle(request):
    if request.method == "PUT":
        data = json.loads(request.body)
        post_id = data.get("post_id")
        username = request.user
        post = Post.objects.get(pk=post_id)
        post_likes = post.likes.all()

        if username in post_likes:
            post.likes.remove(username)
        else:
            post.likes.add(username)
        post.save()
        return JsonResponse({"message": "Like status updated successfully."}, status=201)
    else:
        return JsonResponse({"error": "PUT request required."}, status=400)

@csrf_exempt
@login_required(login_url='login')
def edit(request):
    if request.method == "PUT":
        data = json.loads(request.body)
        post_id = data.get("post_id")
        new_content = data.get("new_content")
        post = Post.objects.get(id=post_id)
        if post.username == request.user.username:
            post.content = new_content
            post.save()
            return JsonResponse({"message": "Post updated successfully."}, status=201)
    else:
        return JsonResponse({"error": "PUT request required."}, status=400)