from pygame import *

for map_name in open("map_list.txt").read().split():
    infile = image.load(map_name + "-c.png")
    outfile = open(map_name + ".txt", "w")

    for y in range(infile.get_height()):
        for x in range(infile.get_width()):
            if infile.get_at((x, y)) == (0, 0, 0):
                outfile.write("O")
            elif infile.get_at((x, y)) == (255, 255, 0):
                outfile.write("_")

            else:
                outfile.write(" ")
            if x == infile.get_width()-1:
                outfile.write("\n")

    outfile.close()