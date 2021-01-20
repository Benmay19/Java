from django.contrib.auth.models import AbstractUser
from django.db import models


class User(AbstractUser):
    pass

class AuctionListing(models.Model):
    owner = models.CharField(max_length=64)
    title = models.CharField(max_length=64)
    description = models.TextField()
    price = models.DecimalField(max_digits=10, decimal_places=2)
    photo = models.ImageField(default=None, blank=True, null=True)
    condition = models.CharField(max_length=12)
    category = models.CharField(max_length=20)
    closed = models.BooleanField(default=False)

    def __str__(self):
        return f"Title: {self.title}, Price: {self.price}"

class Watchlist(models.Model):
    user = models.CharField(max_length=64)
    listing_id = models.IntegerField()

    def __str__(self):
        return f"User: {self.user}, Listing ID: {self.listing_id}"

class Bid(models.Model):
    user = models.CharField(max_length=64)
    title = models.CharField(max_length=64)
    bid = models.DecimalField(max_digits=10, decimal_places=2)
    listing_id = models.IntegerField()

    def __str__(self):
        return f"User: {self.user}, Title: {self.title}, Bid: {self.bid}, Listing ID: {self.listing_id}"

class Comment(models.Model):
    user = models.CharField(max_length=64)
    comment = models.TextField()
    time = models.CharField(max_length=64)
    listing_id = models.IntegerField()

    def __str__(self):
        return f"User: {self.user}, Bid Time: {self.time}, Listing ID: {self.listing_id}"