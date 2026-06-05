.data 0x10010000

str1: .asciiz "Index: "
str2: .asciiz ", Val: "
str3: .asciiz "\n"

.text
.globl main
.eqv PRINT_STR 4
.eqv PRINT_INT 1
.eqv EXIT      10
# loop upper limit:
.eqv MAX 42

main:
  # Setup before entering loop
  # i: $t0 0 index
  # x: $t1 4 first term
  # y: $t2 8 second
  .eqv SIZE_MAIN 12
  addi $sp, $sp, -SIZE_MAIN # Make room in the stack to store internal data

  li   $t2,   1      # Fibbonacci starts with y set to 1 (ex: 0+1)
  sw   $zero, 0($sp) # Index (i) starting value (0) stored in the stack
  sw   $zero, 4($sp) # Loop starts with x as zero
  sw   $t2,   8($sp) # save our initial y

  loop:
    li  $t4, MAX # load the loop's max in a temp buffer
    lw  $a0, 0($sp) # loading i into $a0 saves a step for printing
    bgt $a0, $t4, end
    # Prep for print (i is already setup inside $a0)
    lw  $a1, 4($sp) # load x as the value to print
    jal print

    # Prep and call fib
    lw $a0, 4($sp)
    lw $a1, 8($sp)
    jal fib

    # Prep next iteration
    lw $t0, 0($sp)
    lw $t1, 8($sp) # since x is now becoming y
    addi $t0, $t0, 1 # i++
    sw $t0, 0($sp)
    sw $t1, 4($sp)
    sw $v0, 8($sp) # Store the sum from fib(a,b) as our new greatest value (y)
    j loop

  end:
    addi $sp, $sp, SIZE_MAIN
    li $v0, EXIT
    syscall

fib:
  # $ra: 0 Ret_ADDR
  # $a0: 4 var_a
  # $a1: 8 var_b
  .eqv SIZE_FIB 12
  addi $sp, $sp, -SIZE_FIB
  sw $ra, 0($sp)
  sw $a0, 4($sp)
  sw $a1, 8($sp)

  lw $t0, 4($sp)
  lw $t1, 8($sp)
  add $v0, $t0, $t1 # Save sum in the return

  # Free stack and leave fib
  lw $ra, 0($sp)
  addi $sp, $sp, SIZE_FIB
  jr $ra



print:
  # $ra: 0 Ret_ADDR
  # $a0: 4 Idx
  # $a1: 8 Val
  .eqv SIZE_PRINT 12
  addi $sp, $sp, -SIZE_PRINT # Reseve Stack Space (Print)
  sw $ra, 0($sp)   # store Return addr
  sw $a0, 4($sp)   # store idx param
  sw $a1, 8($sp)   # store val param

  # Print Str1 (idx)
  la $a0, str1
  li $v0, PRINT_STR
  syscall
  # Print idx
  lw $a0, 4($sp)
  li $v0, PRINT_INT
  syscall
  # Print Str2 (val)
  la $a0, str2
  li $v0, PRINT_STR
  syscall
  # Print val
  lw $a0, 8($sp)
  li $v0, PRINT_INT
  syscall
  # Print Str3 (newline)
  la $a0, str3
  li $v0, PRINT_STR
  syscall
  # Free + Go back
  lw $ra, 0($sp)
  addi $sp, $sp, SIZE_PRINT
  jr $ra
