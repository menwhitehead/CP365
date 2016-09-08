import matplotlib.pyplot as plt
import numpy as np
#np.random.seed(42)

def sigmoid(x):
    return 1 / (1 + np.exp(-x))

def dsigmoid(x):
    s = sigmoid(x)
    return s * (1 - s)

class Perceptron:

    def __init__(self, size, learning_rate=0.1):
        self.learning_rate = learning_rate
        self.weights = np.random.rand(size)

    def forward(self, X):
        self.inputs = X
        self.outputs = sigmoid(X.dot(self.weights))
        return self.outputs

    def backward(self, error):
        error = error * dsigmoid(self.outputs)
        self.weight_update = self.inputs.T.dot(error)

    def update(self):
        self.weights += self.learning_rate * self.weight_update

    def calcError(self, targets, prediction):
        return targets - prediction

    def calcMSE(self, X, y):
        predictions = self.forward(X)
        errors = self.calcError(y, predictions)
        # print errors
        return np.sum(np.power(errors, 2)) / len(errors)

    def calcAccuracy(self, X, y):
        outputs = self.forward(X)
        outputs = np.round(outputs)
        correct = np.sum(y == outputs)
        return 100.0 * (correct / float(len(X)))

    def iteration(self, X, y):
        predictions = self.forward(X)
        errors = self.calcError(y, predictions)
        self.backward(errors)
        self.update()

    def train(self, X, y, number_epochs):
        for i in range(number_epochs):
            self.iteration(X, y)
            total_error = self.calcMSE(X, y)
            accuracy = self.calcAccuracy(X, y)
            print "Epoch %d : Error %4.4f : %4.2f%% correct" % (i, total_error, accuracy)


def loadDataset(filename='breast_cancer.csv'):
    my_data = np.genfromtxt(filename, delimiter=',', skip_header=1)

    # The labels of the cases
    # Raw labels are either 4 (cancer) or 2 (no cancer)
    # Normalize these classes to 0/1
    y = (my_data[:, 10] / 2) - 1

    # Case features
    X = my_data[:, :10]

    # Normalize the features to (0, 1)
    X_norm = X / X.max(axis=0)

    return X_norm, y


if __name__=="__main__":
    X, y = loadDataset()
    model = Perceptron(10, 0.1)
    model.train(X, y, 100)
