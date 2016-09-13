import numpy as np
import h5py
import random
from misc_functions import *
from keras.datasets import mnist
from keras.models import Sequential
from keras.layers.core import Dense, Dropout, Activation
from keras.optimizers import SGD, Adam, RMSprop
from keras.utils import np_utils
from keras.models import Sequential
from keras.layers import Dense, Dropout, Activation, Flatten
from keras.layers import Convolution2D, MaxPooling2D
from keras.optimizers import SGD

def createDense():
    model = Sequential()
    model.add(Dense(200, input_shape=(784,)))
    model.add(Activation('sigmoid'))
    model.add(Dense(10))
    model.add(Activation('softmax'))
    model.compile(loss='categorical_crossentropy',
              optimizer=SGD(lr=0.9, momentum=0.0, nesterov=False, decay=0.0),
              metrics=['accuracy'])
    return model

def createConv():
    model = Sequential()

    # First convolutional layer
    model.add(Convolution2D(32, 5, 5, border_mode='valid', input_shape=(1, 28, 28)))
    model.add(Activation('relu'))
    model.add(MaxPooling2D(pool_size=(2, 2)))

    # Second convolutional layer
    model.add(Convolution2D(64, 5, 5))
    model.add(Activation('relu'))
    model.add(MaxPooling2D(pool_size=(2, 2)))

    model.add(Flatten())
    model.add(Dense(1024))
    model.add(Activation('relu'))
    model.add(Dropout(0.5))

    model.add(Dense(10))
    model.add(Activation('softmax'))

    #sgd = SGD(lr=0.1, decay=1e-6, momentum=0.9, nesterov=True)
    model.compile(loss='categorical_crossentropy', optimizer="adam", metrics=['accuracy'])

    return model

def getTrainTest(X, y):
    perm = np.random.permutation(range(len(X)))
    X = X[perm]
    y = y[perm]
    X_train, X_test = X[:len(X)/2], X[len(X)/2:]
    y_train, y_test = y[:len(y)/2], y[len(y)/2:]
    return X_train, X_test, y_train, y_test

def loadDataset1D():
    X1d, y1d = loadMNIST()
    return getTrainTest(X1d, y1d)

def loadDataset2D():
    X2d, y2d = loadMNIST2D()
    return getTrainTest(X2d, y2d)

if __name__ == "__main__":
    # X_train, X_test, y_train, y_test = loadDataset1D()
    # model = createDense()

    X_train, X_test, y_train, y_test = loadDataset2D()
    model = createConv()

    minibatch_size = 32

    model.fit(X_train, y_train,
              batch_size=minibatch_size,
              nb_epoch=100,
              validation_data=(X_test, y_test),
              verbose=1)






#
