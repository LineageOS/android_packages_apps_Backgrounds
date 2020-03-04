import os

from PIL import Image

path = os.path.dirname(os.path.realpath(__file__)) + "/"

resources = ["res_1080p/common/drawable-nodpi"]
resources.append("res_1080p/full/drawable-nodpi")

resources.append("res_1440p/common/drawable-nodpi")
resources.append("res_1440p/full/drawable-nodpi")

def generate_smallvariants(resource):
    global path
    wallpapers_path = path + resource + "/"
    clean(wallpapers_path)
    wallpapers = os.listdir(wallpapers_path)

    for wallpaper in wallpapers:
        # Append _small.jpg to the wallpaper
        wallpaper_small = wallpaper[:-4] + "_small.jpg"
        wallpaper_small_path = wallpapers_path + wallpaper_small

        # Save the wallpaper with 1/5 size to wallpaper_small_path
        with Image.open(wallpapers_path + wallpaper) as img:
            small_width = img.width / 5
            small_height = img.height / 5

            size = int(small_width), int(small_height)

            img_small = img.resize(size, Image.ANTIALIAS)
            img_small.save(wallpaper_small_path, "JPEG")

def clean(wallpapers_path):
    wallpapers = os.listdir(wallpapers_path)

    for wallpaper in wallpapers:
        # Get rid of existing _small variants
        if "_small.jpg" in wallpaper:
            os.remove(wallpapers_path + wallpaper)
            continue

for resource in resources:
    generate_smallvariants(resource)
