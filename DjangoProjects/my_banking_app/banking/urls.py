from django.urls import path

from . import views

urlpatterns = [
    path("", views.index, name="index"),
    path("login", views.login_view, name="login"),
    path("logout", views.logout_view, name="logout"),
    path("register", views.register, name="register"),
    path("balances", views.balances, name="balances"),
    path("recurring", views.recurring, name="recurring"),
    path("details", views.details, name="details"),

    # API Routes
    path("deposit", views.deposit, name="deposit"),
    path("withdrawl", views.withdrawl, name="withdrawl"),
    path("transfer", views.transfer, name="transfer"),
    path("add", views.add, name="add"),
    path("remove/<int:item_id>", views.remove, name="remove")
]