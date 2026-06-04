#!/bin/python3


def main():
    possibilities = [
        [0, 0, 0],
        [0, 0, 5],
        [0, 5, 0],
        [0, 5, 5],
        [5, 0, 0],
        [5, 0, 5],
        [5, 5, 0],
        [5, 5, 5],
    ]
    print(possibilities)
    for set in possibilities:
        Vout = ((8 / 15) * set[0] + (4 / 15) * set[1] + (2 / 15) * set[2]) * 1
        # Vout = ((8 / 15) * set[0] + (4 / 15) * set[1] + (2 / 15) * set[2]) * (
        #     5 / (14 / 3)
        # )
        print(f"V1:{set[0]} V2:{set[1]} V3:{set[2]} Vout:{Vout}")


if __name__ == "__main__":
    main()
