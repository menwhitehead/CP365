
import random
import string

#target = "to be or not to be that is the question".upper()
target = "to be or not to be".upper()


class GA:
    def __init__(self, pop_size):
        self.pop = []
        self.pop_size = pop_size

    def fitness(self, solution):
        count = 0
        for i in range(len(solution)):
            if solution[i] == target[i]:
                count += 1
        return count

    def generateRandomSolution(self):
        length = len(target)
        s = ''
        for i in range(length):
            s += random.choice(string.ascii_uppercase +  ' ')
        return s

    def generateInitPopulation(self):
        for i in range(self.pop_size):
            self.pop.append(self.generateRandomSolution())
        self.generatePopulationFitness()


    def generatePopulationFitness(self):
        self.pop_fitnesses = []
        for solution in self.pop:
            self.pop_fitnesses.append(self.fitness(solution))

    def pickFitParent(self):
        total_fitness = sum(self.pop_fitnesses)
        r = random.randrange(total_fitness)
        ind = -1
        while r > 0:
            ind += 1
            r -= self.pop_fitnesses[ind]
        return self.pop[ind]

    def crossover(self, p1, p2):
        return p1[:len(p1)/2] + p2[len(p2)/2:]

    def mutate(self, child, mutation_rate=0.001):
        new_child = ''
        for i in range(len(child)):
            if random.random() < mutation_rate:
                new_child += random.choice(string.ascii_uppercase +  ' ')
            else:
                new_child += child[i]
        return new_child

    def getBestSolution(self):
        max_ind = self.pop_fitnesses.index(max(self.pop_fitnesses))
        return self.pop[max_ind]

    def generateNewPopulation(self):
        new_pop = []
        for i in range(self.pop_size):
            p1 = self.pickFitParent()
            p2 = self.pickFitParent()
            child = self.crossover(p1, p2)
            child = self.mutate(child)
            new_pop.append(child)
        self.pop = new_pop

    def evolve(self, number_epochs):
        self.generateInitPopulation()
        for i in range(number_epochs):
            self.generateNewPopulation()
            self.generatePopulationFitness()
            if i % 100 == 0:
                print i, self.getBestSolution(), max(self.pop_fitnesses)

if __name__=="__main__":
    ga = GA(300)
    ga.evolve(100000)



























#
