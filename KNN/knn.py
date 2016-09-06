import matplotlib.pyplot as plt
import math
import numpy as np

np.random.seed(42)

def loadDataset(filename="affairs.csv"):
    my_data = np.genfromtxt(filename, delimiter=',', skip_header=1)
    years_married_education = my_data[:, [3,6]]
    number_affairs = my_data[:, 9]
    return years_married_education, number_affairs


def euclideanDistance(x, y):
    s = 0.0
    for i in range(len(x)):
        s += (x[i] - y[i]) ** 2
    return math.sqrt(s)

X, y = loadDataset()
# print X
# print y

# A new person who has been married for 8 years and has 16 years of education
new_person = np.array([.1, 12])

# Find people in the dataset with similar years married and education
# Use their number of affairs to predict the number of affairs of this new person!
K = 3
distances = []
ind = 0
for other_person in X:
    distances.append((euclideanDistance(new_person, other_person), y[ind]))
    ind += 1

distances.sort()

s = 0
for dist, nbaffairs in distances[:K]:
    s += nbaffairs

print float(s)/K





#
