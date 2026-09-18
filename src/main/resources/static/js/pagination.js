const bodies = document.querySelectorAll('.paginated-body');

for (let b = 0; b < bodies.length; b++) {
    const body = bodies[b];
    let pageSize = parseInt(body.dataset.pageSize, 10);
    if (isNaN(pageSize)) {
        pageSize = 5;
    }
    const rows = body.querySelectorAll('.paginated-row');
    const prevBtn = document.querySelector('.paginated-prev[data-target="' + body.id + '"]');
    const nextBtn = document.querySelector('.paginated-next[data-target="' + body.id + '"]');
    const label = document.querySelector('.paginated-label[data-target="' + body.id + '"]');

    let page = 0;
    const pageCount = Math.max(1, Math.ceil(rows.length / pageSize));

    function render() {
        const firstIndexOnPage = page * pageSize;
        const firstIndexOnNextPage = (page + 1) * pageSize;

        for (let i = 0; i < rows.length; i++) {
            if (i >= firstIndexOnPage && i < firstIndexOnNextPage) {
                rows[i].hidden = false;
            } else {
                rows[i].hidden = true;
            }
        }

        if (label) {
            label.textContent = (page + 1) + ' / ' + pageCount;
        }
    }

    render();

    if (prevBtn) {
        prevBtn.addEventListener('click', function () {
            if (page > 0) {
                page--;
            } else {
                page = pageCount - 1;
            }
            render();
        });
    }

    if (nextBtn) {
        nextBtn.addEventListener('click', function () {
            if (page < pageCount - 1) {
                page++;
            } else {
                page = 0;
            }
            render();
        });
    }
}
