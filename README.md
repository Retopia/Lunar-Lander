# Lunar-Lander
 Landing Ships with Genetic Algorithms
 ![Project View](https://i.imgur.com/mdbek3J.png)
 
This is a project I made in Honors Artificial Intelligence class in my Junior year of High School.
 
For this project, I created a "1D" version of the old Lunar Lander game, and used genetic algorithms to get the ships to land by themselves.

This is how I used genetic algorithms in this project:

Each ship has, say, 128 genes

Each gene takes up 50 miliseconds

Each gene can be either 1 or 0

if 1: ship is powered (ship thrusters counteract effects of gravity)

if 0: ship is not powered (pulls down due to gravity)

The population is 8 and every generation, there is a small chance (5%) for a ship's gene to mutate.

The 2 best individuals breed, creating the next 8 children.

I thought this project was really fun, and I'm glad it turned out pretty professional looking! In the future, I want to try to combine genetic algorithms with neural networks, because I think doing this project helped me learn the limits of only using genetic algorithms. If the planet's gravity were changed, or the initial position of the ships were changed, then the genetic algorithm would not work. Perhaps using a NN can fix some of these problems.

