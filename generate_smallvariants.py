#!/usr/bin/env python3
import os

from PIL import Image

path = os.path.dirname(os.path.realpath(__file__))

resources = ["res_1080p/drawable-nodpi",
             "res_1440p/drawable-nodpi"]

def generate_smallvariants(resource):
    global path
    wallpapers_path = os.path.join(path, resource)
    clean(wallpapers_path)
    wallpapers = os.listdir(wallpapers_path)

    for wallpaper in wallpapers:
        # Append _small.jpg to the wallpaper
        wallpaper_small = os.path.splitext(wallpaper)[0] + "_small.jpg"
        wallpaper_small_path = os.path.join(wallpapers_path, wallpaper_small)

        # Save the wallpaper with 1/4 size to wallpaper_small_path
        with Image.open(os.path.join(wallpapers_path, wallpaper)) as img:
            size = int(img.width / 4), int(img.height / 4)

            img_small = img.resize(size, Image.ANTIALIAS)
            img_small.save(wallpaper_small_path, "JPEG")

def clean(wallpapers_path):
    wallpapers = os.listdir(wallpapers_path)

    for wallpaper in wallpapers:
        # Get rid of existing small variants
        if wallpaper.endswith("_small.jpg"):
            os.remove(os.path.join(wallpapers_path, wallpaper))

for resource in resources:
    generate_smallvariants(resource)
