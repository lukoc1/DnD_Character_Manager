
const prevBtn = document.querySelector('#prevPage');
const nextBtn = document.querySelector('#nextPage');

const list = document.querySelector(".slider");

const listElements = list.querySelectorAll('.container');

let index = 0;

console.log(listElements);

listElements[index].classList.add("visible");

nextBtn.addEventListener("click", function () {
    console.log("Next");
    listElements[index].classList.remove("visible");

    if (index < listElements.length - 1) {
        index++;
    } else {
        index = 0;
    }
    listElements[index].classList.add("visible");


})

prevBtn.addEventListener("click", function () {
    console.log("Prev");
    listElements[index].classList.remove("visible");

    if (index > 0) {
        index--;
    } else {
        index = listElements.length - 1;
    }
    listElements[index].classList.add("visible");


})

