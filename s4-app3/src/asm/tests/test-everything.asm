.data

base_val: .word 0xDEAD
base_add: .word 0xBEEF
out_norm: .word 0
sum_norm: .word 0

#                B, E, N, C
ben_vec:   .word 66,69,78,67
lower_vec: .word 32,32,32,32
out_vec:   .word 0,0,0,0
min_val:   .word 0

.eqv GRACEFUL_STOP 10

.globl main

.text

main:
la $a0, 0x10010000
la $a1, 0x100100F8
lw $t0, 0($a0)
sw $t0, 0($a1)
