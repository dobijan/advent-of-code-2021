pub mod test;

pub struct Report {
    n_bits: usize,
    numbers: Vec<u16>,
}

use std::collections::HashSet;
impl Report {
    // create new report from puzzle lines
    fn new(lines: Vec<String>) -> Self {
        Self {
            n_bits: lines[0].trim().len(),
            numbers: lines.iter().fold(Vec::new(), |mut ns, line| {
                if let Some(n) = u16::from_str_radix(line.trim(), 2).ok() {
                    ns.push(n)
                }
                ns
            }),
        }
    }

    // returns a set of bit indices where 1 is more common
    fn mcb(&self) -> HashSet<usize> {
        BitStats::new(
            &self.n_bits,
            self.numbers.iter().collect::<Vec<&u16>>().iter(),
        )
        .to_set()
    }

    // task 1
    fn power_consumption(&self) -> u64 {
        let mcb = self.mcb();
        self.gamma_rate(&mcb) * self.epsilon_rate(&mcb)
    }

    fn gamma_rate(&self, mcb: &HashSet<usize>) -> u64 {
        (0..self.n_bits)
            .filter(|pos| mcb.contains(pos))
            .map(|pos| u64::pow(2, (self.n_bits - 1 - pos) as u32))
            .sum()
    }

    fn epsilon_rate(&self, mcb: &HashSet<usize>) -> u64 {
        (0..self.n_bits)
            .filter(|pos| !mcb.contains(pos))
            .map(|pos| u64::pow(2, (self.n_bits - 1 - pos) as u32))
            .sum()
    }

    // task 2
    fn life_support_rating(&self) -> u64 {
        *self.oxygen_generator_rating() as u64 * *self.co2_scrubber_rating() as u64
    }

    fn oxygen_generator_rating<'a>(&'a self) -> &u16 {
        self.find_number(self.numbers.iter().collect(), 0, |depth, bit_stats| {
            MostCommonCriterion::new(CriterionContext::new(depth, bit_stats, &self.n_bits))
        })
    }

    fn co2_scrubber_rating(&self) -> &u16 {
        self.find_number(self.numbers.iter().collect(), 0, |depth, bit_stats| {
            LeastCommonCriterion::new(CriterionContext::new(depth, bit_stats, &self.n_bits))
        })
    }

    // recursively finds the number that satisfies the depth-dependent criterion
    fn find_number<'a, C>(
        &'a self,
        numbers: Vec<&'a u16>,
        depth: usize,
        criterion: impl Fn(usize, BitStats) -> C,
    ) -> &'a u16
    where
        C: Criterion,
    {
        if numbers.len() < 2 {
            numbers[0]
        } else {
            let c = criterion(depth, BitStats::new(&self.n_bits, numbers.iter()));
            self.find_number(
                numbers.into_iter().filter(|n| c.test(n)).collect(),
                depth + 1,
                criterion,
            )
        }
    }
}

trait Criterion {
    fn test(&self, n: &u16) -> bool;
}

struct CriterionContext<'a> {
    depth: usize,
    bit_stats: BitStats,
    n_bits: &'a usize,
}

impl<'a> CriterionContext<'a> {
    fn new(depth: usize, bit_stats: BitStats, n_bits: &'a usize) -> Self {
        Self {
            depth,
            bit_stats,
            n_bits,
        }
    }

    // "abstract criterion" that calculates the current digit and whether the most common bit at the current position is '1'
    // and then calls the criterion-specific comparator
    fn criterion(&self, n: &u16, cmp: fn(bool, u16) -> bool) -> bool {
        let depth_in_bit_set = self.bit_stats.to_set().contains(&self.depth);
        let digit = (n / u16::pow(2, (self.n_bits - 1 - self.depth) as u32)) % 2;
        cmp(depth_in_bit_set, digit)
    }
}

struct MostCommonCriterion<'a> {
    ctx: CriterionContext<'a>,
}

impl<'a> MostCommonCriterion<'a> {
    fn new(ctx: CriterionContext<'a>) -> Self {
        Self { ctx }
    }
}

struct LeastCommonCriterion<'a> {
    ctx: CriterionContext<'a>,
}

impl<'a> LeastCommonCriterion<'a> {
    fn new(ctx: CriterionContext<'a>) -> Self {
        Self { ctx }
    }
}

// implements filter logic when selecting most common bits at each depth
impl<'a> Criterion for MostCommonCriterion<'a> {
    fn test(&self, n: &u16) -> bool {
        self.ctx.criterion(n, |depth_in_bit_set, digit| {
            depth_in_bit_set && digit != 0 || !depth_in_bit_set && digit == 0
        })
    }
}

// implements filter logic when selecting least common bits at each depth
impl<'a> Criterion for LeastCommonCriterion<'a> {
    fn test(&self, n: &u16) -> bool {
        self.ctx.criterion(n, |depth_in_bit_set, digit| {
            !depth_in_bit_set && digit != 0 || depth_in_bit_set && digit == 0
        })
    }
}

// struct for holding bit statistics: how many ones and zeros are there at each bit position
struct BitStats {
    bits: Vec<(u16, u16)>,
}

impl BitStats {
    // computes bit statistics from a list of numbers
    fn new<'a, I>(n_bits: &usize, numbers: I) -> Self
    where
        I: Iterator<Item = &'a &'a u16>,
    {
        Self {
            bits: numbers
                .map(|n| {
                    (0..*n_bits)
                        .map(move |idx| (*n / u16::pow(2, (n_bits - 1 - idx) as u32) % 2) as u8)
                })
                .fold(BitStats::init_acc(*n_bits), |acc, digits| {
                    digits.enumerate().fold(acc, |mut bit_stats, (idx, digit)| {
                        let (zeros, ones) = bit_stats[idx];
                        if digit == 0 {
                            bit_stats[idx] = (zeros + 1, ones)
                        } else {
                            bit_stats[idx] = (zeros, ones + 1)
                        }
                        bit_stats
                    })
                }),
        }
    }

    fn init_acc(n_bits: usize) -> Vec<(u16, u16)> {
        let mut vec = Vec::new();
        for _ in 0..n_bits {
            vec.push((0, 0));
        }
        vec
    }

    // converts bit statistics to a set of indices: if '1' is the more common bit at a given position, the position's index is in the set
    fn to_set(&self) -> HashSet<usize> {
        self.bits
            .iter()
            .enumerate()
            .filter(|(_, (zeros, ones))| zeros <= ones)
            .map(|(idx, _)| idx)
            .collect()
    }
}

pub fn solve(path: &str) -> u64 {
    use super::utils::read_lines;

    match read_lines(path) {
        None => 0,
        Some(lines) => Report::new(lines).power_consumption(),
    }
}

pub fn solve_2(path: &str) -> u64 {
    use super::utils::read_lines;

    match read_lines(path) {
        None => 0,
        Some(lines) => Report::new(lines).life_support_rating(),
    }
}
