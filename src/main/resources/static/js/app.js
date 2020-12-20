$(document).ready(() => {
    $("#addFieldsBtn").click(function () {
        $($('.authors-fields-group')[0]).clone().appendTo($('.author-fields'));
    });


    $("#filterForm").submit(async function (e) {
        e.preventDefault();
        const formData = $(this).serialize();

        const response = await fetch(`${API_URL}/filter?${formData}`);
        const filteredBooks = await response.json();

        const readyBooks = [];
        filteredBooks.forEach(book =>
            readyBooks.push(createBookCard({ ...book })));

        $(".books-wrapper").empty().append(readyBooks);
    });
});