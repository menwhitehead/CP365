import matplotlib.pyplot as plt
import numpy as np
np.random.seed(42) # Get the same random numbers every time


# Ugly code for thinking about linear regression with gradient descent

################################################################
### Load the dataset
my_data = np.genfromtxt('gpa.csv', delimiter=';', skip_header=1)[:10]
hs_gpa = my_data[:, 0]
col_gpa = my_data[:, 1]



################################################################
### Init the model parameters
weight = np.random.rand(1)
bias = np.random.rand(1)
print weight, bias


################################################################
### How do we change the weight and the bias to make the line's fit better?
learning_rate = 0.05

init_cost = np.sum(np.power((hs_gpa*weight+bias) - col_gpa, 2))

error = (hs_gpa*weight+bias) - col_gpa

weight = weight - np.sum(learning_rate * error * hs_gpa / len(hs_gpa))
weight = weight - np.sum(learning_rate * error * 1.0 / len(hs_gpa))

end_cost = np.sum(np.power((hs_gpa*weight+bias) - col_gpa, 2))

error = (hs_gpa*weight+bias) - col_gpa

weight = weight - np.sum(learning_rate * error * hs_gpa / len(hs_gpa))
weight = weight - np.sum(learning_rate * error * 1.0 / len(hs_gpa))

end_end_cost = np.sum(np.power((hs_gpa*weight+bias) - col_gpa, 2))

print init_cost
print end_cost
print end_end_cost

################################################################
### Graph the dataset along with the line defined by the model
#
# xs = np.arange(0, 5)
# ys = xs * weight + bias
#
# plt.plot(hs_gpa, col_gpa, 'r+', xs, ys, 'g-')
# plt.show()
