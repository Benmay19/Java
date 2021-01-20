from django.urls import path

from . import views

urlpatterns = [
    path("", views.index, name="index"),
    path("login", views.login_view, name="login"),
    path("logout", views.logout_view, name="logout"),
    path("register", views.register, name="register"),
    path("create", views.create, name="create"),
    path("listing/<int:id>", views.listing, name="listing"),
    path("watchlist/<str:username>", views.watchlist, name="watchlist"),
    path("remove_watchlist", views.remove_watchlist, name="remove_watchlist"),
    path("add_watchlist", views.add_watchlist, name="add_watchlist"),
    path("close_auction", views.close_auction, name="close_auction"),
    path("place_bid", views.place_bid, name="place_bid"),
    path("comment", views.comment, name="comment"),
    path("categories", views.categories, name="categories"),
    path("category/<str:category>", views.category, name="category")
]