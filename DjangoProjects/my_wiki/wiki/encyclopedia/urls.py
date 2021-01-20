from django.urls import path

from . import views


urlpatterns = [
    path("", views.index, name="index"),
    path("wiki/<str:title>", views.wiki, name="wiki"),
    path("search", views.search, name="search"),
    path("new_page", views.new_page, name="new_page"),
    path("wiki/<str:title>/edit_page", views.edit_page, name="edit_page"),
    path("invalid/<str:query>", views.invalid, name="invalid"),
    path("error/<str:title>", views.error, name="error")
]
