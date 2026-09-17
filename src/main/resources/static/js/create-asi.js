const modeSingle = document.getElementById('mode-single');
const modeDouble = document.getElementById('mode-double');
const ability2 = document.getElementById('ability2');

function updateAbility2State() {
    ability2.disabled = modeSingle.checked;
}

modeSingle.addEventListener('change', updateAbility2State);
modeDouble.addEventListener('change', updateAbility2State);
updateAbility2State();
