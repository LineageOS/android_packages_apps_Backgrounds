import os

from PIL import Image

path = os.path.dirname(os.path.realpath(__file__))

resources = ["res_1080p/common/drawable-nodpi",
"res_1080p/full/drawable-nodpi",
"res_1440p/common/drawable-nodpi",
"res_1440p/full/drawable-nodpi"]

def generate_smallvariants(resource):
    global path
    wallpapers_path = os.path.join(path, resource)
    clean(wallpapers_path)
    wallpapers = os.listdir(wallpapers_path)

    for wallpaper in wallpapers:
        # Append _small.jpg to the wallpaper
        wallpaper_name = os.path.splitext(wallpaper)
        wallpaper_small = wallpaper_name[0] + "_small" + wallpaper_name[1]
        wallpaper_small_path = os.path.join(wallpapers_path, wallpaper_small)

        # Save the wallpaper with 1/5 size to wallpaper_small_path
        with Image.open(os.path.join(wallpapers_path, wallpaper)) as img:
            small_width = img.width / 5
            small_height = img.height / 5

            size = int(small_width), int(small_height)

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
