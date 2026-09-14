document.querySelectorAll('.paginated-body').forEach(body => {
    const pageSize = parseInt(body.dataset.pageSize, 10) || 5;
    const rows = Array.from(body.querySelectorAll('.paginated-row'));
    const prevBtn = document.querySelector('.paginated-prev[data-target="' + body.id + '"]');
    const nextBtn = document.querySelector('.paginated-next[data-target="' + body.id + '"]');
    const label = document.querySelector('.paginated-label[data-target="' + body.id + '"]');

    let page = 0;
    const pageCount = Math.max(1, Math.ceil(rows.length / pageSize));

    function render() {
        rows.forEach((row, i) => {
            row.hidden = i < page * pageSize || i >= (page + 1) * pageSize;
        });
        if (label) {
            label.textContent = (page + 1) + ' / ' + pageCount;
        }
    }

    render();

    if (prevBtn) {
        prevBtn.addEventListener('click', () => {
            page = page > 0 ? page - 1 : pageCount - 1;
            render();
        });
    }

    if (nextBtn) {
        nextBtn.addEventListener('click', () => {
            page = page < pageCount - 1 ? page + 1 : 0;
            render();
        });
    }
});
