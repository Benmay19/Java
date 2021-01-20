from django.contrib import admin
from .models import AuctionListing, User, Watchlist, Bid, Comment

# Register your models here.
class AuctionListingAdmin(admin.ModelAdmin):
    list_display = ("id", "title", "price", "category")

class WatchlistAdmin(admin.ModelAdmin):
    list_display = ("user", "listing_id")

class BidAdmin(admin.ModelAdmin):
    list_display = ("user", "title", "bid", "listing_id")

class CommentAdmin(admin.ModelAdmin):
    list_display = ("user", "time", "listing_id")

admin.site.register(AuctionListing, AuctionListingAdmin)
admin.site.register(User)
admin.site.register(Watchlist, WatchlistAdmin)
admin.site.register(Bid, BidAdmin)
admin.site.register(Comment, CommentAdmin)