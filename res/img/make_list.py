from os import listdir

out = open("image_list.txt", "w")

out.write("\n".join([img for img in listdir(".")
                if img.endswith(".png")]))

out.close()