from anytree import Node, RenderTree
import json
import sys


def build_tree(obj, name="root"):
    node = Node(name)
    if isinstance(obj, dict):
        for k, v in obj.items():
            child = build_tree(
                v, f"{k}" if isinstance(v, (dict, list)) else f"{k}: {v}"
            )
            child.parent = node
    elif isinstance(obj, list):
        for i, item in enumerate(obj):
            child = build_tree(item, f"[{i}]")
            child.parent = node
    return node


data = json.load(sys.stdin)
tree = build_tree(data)
for pre, _, node in RenderTree(tree):
    print(f"{pre}{node.name}")
