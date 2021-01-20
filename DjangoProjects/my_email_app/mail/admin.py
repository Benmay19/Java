from django.contrib import admin
from .models import Email, User

# Register your models here.
class EmailAdmin(admin.ModelAdmin):
    list_display = ("id", "timestamp")

admin.site.register(Email, EmailAdmin)