# LETHAL HELPER v1.3
This program is made for Lethal Company, a game about venturing into dangerous facilities to gather scrap to sell. After 3 days of collecting, you head to the Company building to sell your scrap to reach your quota. Different objects have different values, and exceeding quota does not carry over, so this program will take the scrap you have and calculate the most efficient combinations of scrap to sell.

The question can be summed up mathematically as: Given a list of values, find a subset of values that sum to >= a target and as as close to the target as possible using the least amount of objects (optional, my program doesn't explicitly focus on this).

This will be horribly inefficient by the way, probably O(n!). It runs fairly quickly when I tested it but it will be exponentially more slow with more data.

This program runs entirely within standard input and output. I haven't learned how to print to a window or powershell yet. I was planning to eventually make this a mod on Thunderstore but someone has already done it

For anyone wanting to use this code, feel free to do so. This is my first time coding in Java, so it's probably not that great. This was a small project for me to get used to coding before starting it in school, but this was in VS Code and we're using BlueJ because the local uni uses BlueJ

v1.1 - General clean-up of the code

v1.2 - Streamlined the code that runs recursiveSearcher by ~90%

v1.3 - Added repetition and deletion of array elements, uploaded to GitHub

v1.4 - Reduced memory load by replacing ints to shorts and bytes
