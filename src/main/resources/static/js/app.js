$(document).ready(() => {
    $("#addFieldsBtn").click(function () {
        $($('.authors-fields-group')[0]).clone().appendTo($('.author-fields'));
    });


    $("#filterForm").submit(async function (e) {
        e.preventDefault();
        const formData = $(this).serialize();

        const response = await fetch(`${API_URL}books/filter?${formData}`);
        const filteredBooks = await response.json();

        const readyBooks = [];
        filteredBooks.forEach(book =>
            readyBooks.push(createBookCard({...book})));

        $(".books-wrapper").empty().append(readyBooks);
    });

    $("#blockInp").click(async function () {
        const parent = $(this).parent().parent().parent();
        const id = $(parent).data("user-id");
        const isBlocked = $(this).is(':checked');
        const res = await fetch(`${API_URL}users/block-user?id=${id}&blocked=${isBlocked}`);
        const result = await res.text();

        if (result === 'ok') {
            $("#blockDate").prop('disabled', !isBlocked);
        }
    })
});