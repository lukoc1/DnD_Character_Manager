const selects = Array.from(document.querySelectorAll('.ability-select'));
const pool = document.getElementById('rolled-pool');
const submitBtn = document.getElementById('submit-btn');
let currentRolls = [];

function d6() {
    return 1 + Math.floor(Math.random() * 6);
}

// roll four d6, drop the lowest = total of the highest three
function rollOne() {
    const dice = [d6(), d6(), d6(), d6()];
    const lowest = Math.min(dice[0], dice[1], dice[2], dice[3]);

    let total = 0;
    let dropped = false;
    for (const value of dice) {
        if (value === lowest && !dropped) {
            dropped = true; // skip the lowest die, only once
        } else {
            total += value;
        }
    }
    return total;
}

// valid only when every ability is set and the picks are exactly the rolled numbers (each used once)
function isValidAssignment() {
    if (currentRolls.length !== 6 || selects.some(s => s.value === '')) {
        return false;
    }
    // currentRolls is already sorted
    const picked = selects.map(s => Number(s.value)).sort((a, b) => a - b).join(',');
    const rolled = currentRolls.join(',');
    return picked === rolled;
}

function refresh() {
    submitBtn.disabled = !isValidAssignment();
}

document.getElementById('roll-btn').addEventListener('click', () => {
    currentRolls = [];
    for (let i = 0; i < 6; i++) {
        currentRolls.push(rollOne());
    }
    currentRolls.sort((a, b) => a - b); // keep sorted so validation can compare directly
    pool.textContent = currentRolls.join(', ');

    selects.forEach((select, i) => {
        select.innerHTML = '<option value="">-</option>';
        for (const roll of currentRolls) {
            select.innerHTML += `<option value="${roll}">${roll}</option>`;
        }
        select.selectedIndex = i + 1; // default: the i-th ability gets the i-th rolled number
    });
    refresh();
});

selects.forEach(s => s.addEventListener('change', refresh));
