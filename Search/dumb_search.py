

class TreeNode:

  def __init__(self, value, parent=None):
      self.value = value
      self.parent = parent
      self.children = []
      if parent != None:
          parent.addChild(self)

  def addChild(self, childNode):
      self.children.append(childNode)

  # def __eq__(self, other):
  #     return self.value == other.value

  def __str__(self):
      return str(self.value)



def bfs(tree, goal):
    q = []
    q.append(tree)
    currNode = q.pop(0)
    while currNode.value != goal:
        print currNode
        for child in currNode.children:
            q.append(child)
        currNode = q.pop(0)

    # currNode is the goal
    goal_path = [currNode.value]
    while currNode.parent != None:
        currNode = currNode.parent
        # goal_path.append(currNode.value)
        goal_path.insert(0, currNode.value)
    return goal_path

def buildTree():
    a = TreeNode('A')
    b = TreeNode('B', a)
    c = TreeNode('C', a)
    d = TreeNode('D', b)
    e = TreeNode('E', b)
    f = TreeNode('F', c)
    return a


if __name__=="__main__":
    t = buildTree()
    print bfs(t, 'E')





#
