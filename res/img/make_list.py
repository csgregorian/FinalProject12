# Creates a list of images that need to be loaded into TextureManager
# in the current directory.

from os import listdir

out = open("image_list.txt", "w")

out.write("\n".join([img.replace(".png", "") for img in listdir(".")
                if img.endswith(".png")]))

out.close()