# Converts PNGs to text files for collision maps
# Requires pygame because other imaging libraries suck

from pygame import *

# Uses the prepopulated map listing
for map_name in open("map_list.txt").read().split():
    # Opens png and corresponding txt
    infile = image.load(map_name + "-c.png")
    outfile = open(map_name + ".txt", "w")

    # Checks each column->row
    for y in range(infile.get_height()):
        for x in range(infile.get_width()):
            # Black is a block, yellow is a powerup spawner
            if infile.get_at((x, y)) == (0, 0, 0):
                outfile.write("O")
            elif infile.get_at((x, y)) == (255, 255, 0):
                outfile.write("_")
            else:
                outfile.write(" ")

            # New line on last character
            if x == infile.get_width()-1:
                outfile.write("\n")

    outfile.close()