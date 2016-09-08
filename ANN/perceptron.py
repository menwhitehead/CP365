import matplotlib.pyplot as plt
import numpy as np
np.random.seed(42)

def sigmoid(x):
    return 1 / (1 + np.exp(-x))

def dsigmoid(x):
    return x * (1 - x)

class Perceptron:

    def __init__(self, size, learning_rate=0.1):
        self.size = size
        self.weights = np.random.rand(size)
        self.learning_rate = learning_rate

    def forward(self, X):
        self.incoming = X
        act = X.dot(self.weights)
        act = sigmoid(act)
        self.outputs = act
        return act

    def backward(self, err):
        err = err * dsigmoid(self.outputs)
        update = self.incoming.T.dot(err)
        self.weights += self.learning_rate * update
        return update

    def reportAccuracy(self, X, y):
        out = self.forward(X)
        out = np.round(out)
        count = np.count_nonzero(y - out)
        correct = len(X) - count
        print "%.4f" % (float(correct)*100.0 / len(X))

    def calculateDerivError(self, y, pred):
        return 2*(y - pred)

    def calculateError(self, y, pred):
        return (np.sum(np.power((y - pred), 2)))

    def iteration(self, X, y):
        out = self.forward(X)
        err = self.calculateError(y, out)
        # print err
        deriv_err = self.calculateDerivError(y, out)
        self.backward(deriv_err)

    def train(self, X, y, number_epochs):
        for i in range(number_epochs):
            self.iteration(X, y)
            self.reportAccuracy(X, y)


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



def gradientChecker(model, X, y):
    epsilon = 1E-5

    model.weights[1] += epsilon
    out1 = model.forward(X)
    err1 = model.calculateError(y, out1)

    model.weights[1] -= 2*epsilon
    out2 = model.forward(X)
    err2 = model.calculateError(y, out2)

    numeric = (err2 - err1) / (2*epsilon)
    print numeric

    model.weights[1] += epsilon
    out3 = model.forward(X)
    err3 = model.calculateDerivError(y, out3)
    derivs = model.backward(err3)
    print derivs[1]




if __name__=="__main__":
    X, y = loadDataset()
    # X = X
    print X
    print y
    print X.shape, y.shape
    model = Perceptron(10, 0.1)
    gradientChecker(model, X, y)
    # model.train(X, y, 100)
