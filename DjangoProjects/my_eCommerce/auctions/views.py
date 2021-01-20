from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from django.db import IntegrityError
from django.http import HttpResponse, HttpResponseRedirect
from django import forms
from django.shortcuts import render
from django.urls import reverse
from .models import AuctionListing, Watchlist, Bid, Comment
from datetime import datetime

from .models import User


def index(request):
    return render(request, "auctions/index.html", {
        "auctions": AuctionListing.objects.all()
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
            return render(request, "auctions/login.html", {
                "message": "Invalid username and/or password."
            })
    else:
        return render(request, "auctions/login.html")


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
            return render(request, "auctions/register.html", {
                "message": "Passwords must match."
            })

        # Attempt to create new user
        try:
            user = User.objects.create_user(username, email, password)
            user.save()
        except IntegrityError:
            return render(request, "auctions/register.html", {
                "message": "Username already taken."
            })
        login(request, user)
        return HttpResponseRedirect(reverse("index"))
    else:
        return render(request, "auctions/register.html")

@login_required(login_url='login')
def create(request):
    if request.method == "POST":
        owner = request.user.username
        title = request.POST['title']
        description = request.POST['description']
        price = request.POST['price']
        photo = request.POST['photo']
        condition = request.POST['condition']
        category = request.POST['category']

        new_listing = AuctionListing(owner=owner, title=title, description=description, price=price, photo=photo, condition=condition, category=category)
        new_listing.save()
        return HttpResponseRedirect(reverse("index"))

    else:
        return render(request, "auctions/create.html")

def close_auction(request):
    if request.user.username:
        listing_id = request.POST["listing_id"]
        listing = AuctionListing.objects.get(id=listing_id)
        listing.closed = True
        listing.save()
        return HttpResponseRedirect(f"/listing/{listing_id}")


def listing(request, id):
    listing = AuctionListing.objects.get(id=id)

    if request.user.username:
        try:
            if Watchlist.objects.get(user=request.user.username, listing_id=id):
                added = True
        except:
            added = False
        try:
            listing = AuctionListing.objects.get(id=id)
            if listing.owner == request.user.username:
                owner = True
            else:
                owner = False
        except:
            return HttpResponseRedirect(reverse("index"))
    else:
        added = False
        owner = False
    try:
        if Bid.objects.get(listing_id=id) is not None:
            bid = Bid.objects.get(listing_id=id)
            is_bid = True
    except:
        bid = None
        is_bid = False
    try:
        bids = Bid.objects.filter(listing_id=id)
        bid_count = len(bids)
    except:
        bid_count = None
    try:
        comments = Comment.objects.filter(listing_id=id)
    except:
        comments = None
    return render(request, "auctions/listing.html", {
        "listing": listing,
        "added": added,
        "owner": owner,
        "bid": bid,
        "is_bid": is_bid,
        "error": request.COOKIES.get("error"),
        "success": request.COOKIES.get("success"),
        "bid_count": bid_count,
        "comments": comments
    })

def add_watchlist(request):
    if request.user.username:
        watchlist = Watchlist()
        watchlist.user = request.user.username
        listing_id = request.POST["listing_id"]
        watchlist.listing_id = listing_id
        watchlist.save()
        return HttpResponseRedirect(f"/listing/{listing_id}")
    else:
        return render(request, "auctions/index.html")

def remove_watchlist(request):
    if request.user.username:
        try:
            listing_id = request.POST["listing_id"]
            watchlist = Watchlist.objects.get(user=request.user.username, listing_id=listing_id)
            watchlist.delete()
            return HttpResponseRedirect(f"/listing/{listing_id}")
        except:
            return HttpResponseRedirect(f"/listing/{listing_id}")
    else:
        return render(request, "auctions/index.html")

def watchlist(request, username):
    if request.user.username:
        watchlist = Watchlist.objects.filter(user=username)
        listings = []
        for listing in watchlist:
            listings.append(AuctionListing.objects.get(id=listing.listing_id))
        return render(request, "auctions/watchlist.html", {
            "listings": listings
        })
    else:
        return render(request, "auctions/index.html", {
            "auctions": AuctionListing.objects.all()
        })

def place_bid(request):
    if request.method == "POST":
        listing_id = request.POST["listing_id"]
        user = request.user.username
        listing = AuctionListing.objects.get(id=listing_id)
        title = listing.title
        new_bid = float(request.POST["bid"])
        try:
            if Bid.objects.get(listing_id=listing_id):
                current_bid = Bid.objects.get(listing_id=listing_id)
                if new_bid <= current_bid.bid:
                    message = HttpResponseRedirect(f"/listing/{listing_id}")
                    message.set_cookie("error", "ERROR: Bid must be greater than current bid.", max_age=1)
                    return message
                else:
                    current_bid.bid = new_bid
                    current_bid.user = user
                    current_bid.save()
                    listing.price = new_bid
                    listing.save()
                    message = HttpResponseRedirect(f"/listing/{listing_id}")
                    message.set_cookie("success", "Your bid was placed successfully!", max_age=1)
                    return message
        except:
            if new_bid < listing.price:
                message = HttpResponseRedirect(f"/listing/{listing_id}")
                message.set_cookie("error", "ERROR: Bid must be greater than or equal to the starting bid.", max_age=1)
                return message
            else:
                place_bid = Bid(user=user, title=title, bid=new_bid, listing_id=listing_id)
                place_bid.save()
                listing.price = new_bid
                listing.save()
                message = HttpResponseRedirect(f"/listing/{listing_id}")
                message.set_cookie("success", "Your bid was placed successfully!", max_age=1)
                return message
    else:
        return render(request, "auctions/index.html")

def comment(request):
    if request.method == "POST":
        listing_id = request.POST["listing_id"]
        new_comment = request.POST["comment"]
        user = request.user.username
        time = datetime.now()
        comment_time = time.strftime(" %b-%d-%Y %X ")
        comment = Comment(user=user, comment=new_comment, time=comment_time, listing_id=listing_id)
        comment.save()
        return HttpResponseRedirect(f"/listing/{listing_id}")
    else:
        return render(request, "auctions/index.html")

def categories(request):
    listings = AuctionListing.objects.all()
    categories =[]
    for listing in listings:
        category = listing.category 
        if category not in categories:
            categories.append(category)
    return render(request, "auctions/categories.html", {
        "categories": categories
    })

def category(request, category):
    listings = AuctionListing.objects.filter(category=category)
    return render(request, "auctions/category.html", {
        "listings": listings,
        "category": category
    })
