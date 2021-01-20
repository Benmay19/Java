from django.contrib import admin
from .models import User, Financial, Recurring

# Register your models here.
class UserAdmin(admin.ModelAdmin):
    list_display = ("id", "username")

class FinancialAdmin(admin.ModelAdmin):
    list_display = ("user", "checking", "savings")

class RecurringAdmin(admin.ModelAdmin):
    list_display = ("user", "name", "amount")


admin.site.register(User, UserAdmin)
admin.site.register(Financial, FinancialAdmin)
admin.site.register(Recurring, RecurringAdmin)