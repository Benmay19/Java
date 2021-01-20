from django.contrib.auth.models import AbstractUser
from django.db import models


class User(AbstractUser):
    pass

class Post(models.Model):
    username = models.CharField(max_length=64)
    content = models.TextField(blank=True)
    timestamp = models.DateTimeField(auto_now_add=True)
    likes = models.ManyToManyField("User", default=None, blank=True, related_name="likes")

    def serialize(self):
        return {
            "id": self.id,
            "username": self.username,
            "content": self.content,
            "timestamp": self.timestamp.strftime("%b %d %Y, %I:%M %p"),
            "likes": self.likes
        }

class Profile(models.Model):
    username = models.ForeignKey("User", on_delete=models.CASCADE)
    following = models.ManyToManyField("User", default=None, blank=True, related_name="following")
    followers = models.ManyToManyField("User", default=None, blank=True, related_name="followers")