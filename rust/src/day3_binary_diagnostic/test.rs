#![cfg(test)]

const PATH: &str = "puzzles/day3/input";

#[test]
pub fn day3_task1() {
    println!(
        "[Day 3 Task 1] Submarine power consumption: {}",
        super::solve(PATH)
    )
}

#[test]
pub fn day3_task2() {
    println!(
        "[Day 3 Task 2] Submarine life support rating: {}",
        super::solve_2(PATH)
    )
}
