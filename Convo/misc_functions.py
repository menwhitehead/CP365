import numpy as np
import h5py
import math
import random

DATASETS_DIR = "."

def not1DHorizontal(x):
    "When arrays get to be 1D, make 'em 2D instead'"
    if x.ndim == 1:
        x = np.reshape(x, (1, len(x)))
    return x

def not1DVertical(x):
    "When arrays get to be 1D, make 'em 2D instead'"
    if x.ndim == 1:
        x = np.reshape(x, (len(x), 1))
    return x

def glorotUniformWeights(number_incoming, number_outgoing):
    weight_range = math.sqrt(12.0 / (number_incoming + number_outgoing))
    #self.weights = np.random.normal(0, weight_range, (number_incoming, number_outgoing))
    #self.weights = (0.5 - np.random.rand(number_incoming, number_outgoing)) * weight_range
    return weight_range - (2 * weight_range * np.random.rand(number_incoming, number_outgoing))

def glorotNormalWeights(number_incoming, number_outgoing):
    weight_range = math.sqrt(12.0 / (number_incoming + number_outgoing))
    return np.random.normal(0, weight_range, (number_incoming, number_outgoing))

def normalWeights(number_incoming, number_outgoing):
    return np.random.normal(0, 1.0, (number_incoming, number_outgoing))

def zeroWeights(number_incoming, number_outgoing):
    return np.zeros((number_incoming, number_outgoing))


def convertToOneHot(val, size):
    x = np.zeros(size)
    x[val] = 0.9
    return x

def convertToBinary(val, size):
    x = np.zeros(size)
    bin_rep = bin(val)[2:]  # get a binary string
    # print bin_rep
    start_ind = size - len(bin_rep)
    for i in range(len(bin_rep)):
        if int(bin_rep[i]) == 1:
            x[i+start_ind] = 1.0
    return x

def binaryToInt(binary):
    bin_str = ''
    for val in binary:
        if val == 1.0:
            bin_str += '1'
        else:
            bin_str += '0'
    # print bin_str
    return int(bin_str, 2)


def loadXOR():
    X = np.array([ [0,0,1],[0,1,1],[1,0,1],[1,1,1] ])
    y = np.array([[0,1,1,0]]).T
    return X, y

def loadXORTanh():
    X = np.array([ [-1,-1,1],[-1,1,1],[1,-1,1],[1,1,1] ])
    y = np.array([[-1,1,1,-1]]).T
    return X, y


def loadAdditionOneHot(number_problems=100, max_number=10):
    xs = []
    ys = []
    for i in range(number_problems):
        operand1 = random.randrange(max_number)
        operand2 = random.randrange(max_number)
        xs.append(np.append(convertToOneHot(operand1, max_number), convertToOneHot(operand2, max_number)))
        ys.append(convertToOneHot(operand1 + operand2, max_number * 2))
    X = np.array(xs)
    y = np.array(ys)

    return X, y

def loadAdditionBinary(number_problems=100, operand_bits=8):
    xs = []
    ys = []
    for i in range(number_problems):
        operand1 = random.randrange(2**operand_bits)
        operand2 = random.randrange(2**operand_bits)
        xs.append(np.append(convertToBinary(operand1, operand_bits), convertToBinary(operand2, operand_bits)))
        ys.append(convertToBinary(operand1 + operand2, operand_bits * 2))
    X = np.array(xs)
    y = np.array(ys)

    return X, y


def loadAdditionPlusOneBinary(number_problems=100, operand_bits=8):
    xs = []
    ys = []
    for i in range(number_problems):
        operand1 = random.randrange(2**operand_bits)
        operand2 = random.randrange(2**operand_bits)
        xs.append(np.append(convertToBinary(operand1, operand_bits), convertToBinary(operand2, operand_bits)))
        ys.append(convertToBinary(operand1 + operand2 + 1, operand_bits * 2))
    X = np.array(xs)
    y = np.array(ys)

    return X, y



def loadBreastCancer():
    size = 263
    f = h5py.File(DATASETS_DIR + "breast_cancer.hdf5", 'r')
    X = f['data']['data'][:].T[:size]
    # np.array([f['t_train'][:size]]).T
    y = np.array([f['data']['label'][:size]]).T

    littles = np.amin(X, axis=0)
    bigs = np.amax(X, axis=0)

    X = (X - littles) / (bigs - littles)
    #print X

    y = (y + 1) / 2

    print "Breast Cancer Dataset LOADED", X.shape, y.shape

    return X, y


def loadBreastCancerTanh():
    size = 263
    f = h5py.File(DATASETS_DIR + "breast_cancer.hdf5", 'r')
    X = f['data']['data'][:].T[:size]
    # np.array([f['t_train'][:size]]).T
    y = np.array([f['data']['label'][:size]]).T

    littles = np.amin(X, axis=0)
    bigs = np.amax(X, axis=0)

    X = 1 - (2 * (X - littles) / (bigs - littles))

    print "Breast Cancer Dataset LOADED", X.shape, y.shape

    return X, y


def loadMNIST():
    size = 10000
    f = h5py.File("mnist.hdf5", 'r')
    X = f['x_train'][:size]

    maxes = X.max(axis=0)
    for i in range(len(maxes)):
        if maxes[i] == 0:
            maxes[i] = 0.1
    X *= 1/maxes
    # print X.shape

    raw_y = np.array([f['t_train'][:size]]).T

    y = []
    for row in raw_y:
        y.append(convertToOneHot(row[0], 10))

    y = np.array(y)

    print "MNIST Dataset LOADED"

    return X, y

def loadMNIST2D():
    X, y = loadMNIST()
    # Convert the X into 2-D images

    width = 28
    height = 28

    number_samples = len(X)

    X = np.reshape(X, (number_samples, 1, width, height))

    print "MNIST2D Dataset LOADED"

    return X, y


# Testing model accuracies
def accuracyBinary(model, X, y, train=False):
    # Make a dictionary of inputs if the model is a Graph
    if model.__class__.__name__ == "Graph":
        inputs = {"input1": X}
    else:
        inputs = X

    outputs = model.forward(inputs, train=train)
    outputs = np.round(outputs)
    correct = np.sum(y == outputs)
    return 100.0 * (correct / float(len(X)))


def accuracy(model, X, y):

    # Make a dictionary of inputs if the model is a Graph
    if model.__class__.__name__ == "Graph":
        inputs = {"input1": X}
    else:
        inputs = X

    dataset_size = len(X)
    correct = 0
    output = model.forward(inputs, train=False)
    for ind in range(dataset_size):
        curr_out = output[ind]
        max_ind = list(curr_out).index(np.max(curr_out))
        tar_ind = list(y[ind]).index(np.max(y[ind]))
        if max_ind == tar_ind:
            correct += 1

    return "\t*** Accuracy: %.2f%% ***" % (100.0 * (correct / float(dataset_size)))




weight_inits = {'glorot_normal':glorotNormalWeights,
                'glorot_uniform':glorotUniformWeights,
                'normal':normalWeights,
                'zeros':zeroWeights}


if __name__ == "__main__":
    x = 0.5 - np.random.rand(10)
    print x
    print relu(x)
    print drelu(x)
