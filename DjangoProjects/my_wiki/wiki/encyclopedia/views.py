import markdown2
import random
from django import forms
from django.shortcuts import render
from django.http import HttpResponseRedirect
from django.urls import reverse
from django.utils.safestring import mark_safe

from . import util

class NewPageForm(forms.Form):
    title = forms.CharField(label="New Page Title")
    content = forms.CharField(widget=forms.Textarea(attrs={"placeholder": "Enter page content...", "cols":50}), label="")

class EditPageForm(forms.Form):
    content = forms.CharField(widget=forms.Textarea(attrs={"cols":50}), label="New Content")

def entries_lower():
    entries_low = [entry.lower() for entry in util.list_entries()]
    return entries_low

def random_page():
    entries = util.list_entries()
    entry = entries[random.randint(0, len(entries)-1)]
    random_url = entry
    return random_url

def index(request):
    return render(request, "encyclopedia/index.html", {
        "entries": util.list_entries(),
        "random_url": f"wiki/{random_page()}"
    })

def wiki(request, title):
    if title.lower() in entries_lower():
        return render(request, "encyclopedia/wiki.html", {
            "entries": markdown2.markdown(util.get_entry(title)),
            "random_url": random_page(),
            "title": title
        })
    else:
        return HttpResponseRedirect(f"/invalid/{title}")

def search(request):
    entries = util.list_entries()
    sub_entries = []

    search_box = request.POST.get("q", "").lower()

    if search_box in entries_lower():
        return HttpResponseRedirect(f"wiki/{search_box}")

    for entry in entries:
        if search_box in entry.lower():
            sub_entries.append(entry)

    if sub_entries:
        return render(request, "encyclopedia/search.html", {
            "search_result": sub_entries,
            "search": search_box,
            "random_url": f"wiki/{random_page()}"
        })
    else:
        return render(request, "encyclopedia/search.html", {
            "no_result": f"No results for '{search_box}'",
            "random_url": f"wiki/{random_page()}"
        })

def new_page(request):
    if request.method == "POST":
        form = NewPageForm(request.POST)
        if form.is_valid():
            title = form.cleaned_data['title']
            content = form.cleaned_data['content']
            if title.lower() in entries_lower():
                return HttpResponseRedirect(f"/error/{title}") 
            else:
                new_page = util.save_entry(title, content)
                return HttpResponseRedirect(f"wiki/{title}")
        else:
            return render(request, "encyclopedia/new_page.html", {
                "form": form
            })
    return render(request, "encyclopedia/new_page.html", {
        "form": NewPageForm(),
        "random_url": f"wiki/{random_page()}"
    })

def edit_page(request, title):
    entry = {"content": util.get_entry(title)}
    if request.method == "POST":
        form = EditPageForm(request.POST)
        if form.is_valid():
            content = form.cleaned_data['content']
            updated_page = util.save_entry(title, content)
            return HttpResponseRedirect(f"/wiki/{title}")

    return render(request, "encyclopedia/edit_page.html", {
        "form": EditPageForm(entry),
        "title": title,
        "random_url": f"/wiki/{random_page()}"
    })

def invalid(request, query):
    return render(request, "encyclopedia/invalid.html", {
        "query": query,
        "random_url": f"/wiki/{random_page()}"
    })

def error(request, title):
    return render(request, "encyclopedia/error.html", {
        "title": title,
        "random_url": f"/wiki/{random_page()}"
    })