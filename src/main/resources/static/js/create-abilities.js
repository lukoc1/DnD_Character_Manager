const selects = Array.from(document.querySelectorAll('.ability-select'));
const rolledNums = document.getElementById('rolled-nums');
const submitBtn = document.getElementById('submit-btn');
const rollBtn = document.getElementById('roll-btn');

let currentRolls = [];

function d6() {
    return Math.floor(Math.random() * 6) + 1;
}

// roll four d6, drop the lowest = total of the highest three
function rollOne() {
    const dice = [d6(), d6(), d6(), d6()].sort((a, b) => a - b);
    // dice[0] is the lowest after sorting, so sum the remaining three
    return dice[1] + dice[2] + dice[3];
}

// valid only when every ability is set and the picks are exactly the rolled numbers (each used once)
function isValidAssignment() {
    if (currentRolls.length !== 6) {
        return false;
    }

    for (let i = 0; i < selects.length; i++) {
        if (selects[i].value === '') {
            return false;
        }
    }

    const picked = [];
    for (let i = 0; i < selects.length; i++) {
        picked.push(selects[i].value);
    }
    picked.sort((a, b) => a - b);

    return picked.join(',') === currentRolls.join(',');
}

function refresh() {

    if (isValidAssignment()) {
        submitBtn.disabled = false;
    } else {
        submitBtn.disabled = true;
    }
}

rollBtn.addEventListener('click', function () {

    currentRolls = [];

    for (let i = 0; i < 6; i++) {
        currentRolls.push(rollOne());
    }

    currentRolls.sort((a, b) => a - b);
    rolledNums.textContent = currentRolls.join(', ');

    for (let i = 0; i < selects.length; i++) {
        const select = selects[i];
        select.innerHTML = '<option value="">-</option>';

        for (let j = 0; j < currentRolls.length; j++) {
            const roll = currentRolls[j];
            select.innerHTML += `<option value="${roll}">${roll}</option>`;
        }
        select.selectedIndex = i + 1;
    }
    refresh();
});

selects.forEach(s => s.addEventListener('change', refresh));
