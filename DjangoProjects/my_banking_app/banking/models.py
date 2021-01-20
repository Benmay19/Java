from django.contrib.auth.models import AbstractUser
from django.db import models


class User(AbstractUser):
    pass

class Financial(models.Model):
    user = models.ForeignKey("User", on_delete=models.CASCADE)
    checking = models.DecimalField(max_digits=10, decimal_places=2, default=0.00)
    savings = models.DecimalField(max_digits=10, decimal_places=2, default=0.00)

    def __str__(self):
        return f"User: {self.user}, Checking: {self.checking}, Savings: {self.savings}"

class Recurring(models.Model):
    user = models.ForeignKey("User", on_delete=models.CASCADE)
    account = models.CharField(max_length=8, default="checking")
    bill = models.BooleanField()
    name = models.CharField(max_length=64)
    amount = models.DecimalField(max_digits=6, decimal_places=2)
    day = models.IntegerField()

    def __str__(self):
        return f"User: {self.user}, Name: {self.name}, Amount: {self.amount}, Day: {self.day}, Account: {self.account}"