const createBookCard = ({ id, title, date, genre, price, authors }) => {
    return $(`<div class="card book" style="width: 18rem;">
            <img src="https://pngimg.com/uploads/book/book_PNG2105.png" class="card-img-top" alt="book image">
            <div class="card-body">
                <a href="/book/${id}">
                    <h5 class="card-title">${title}</h5>
                </a>
                <h6 class="card-title">${genre.name}</h6>
                <div class="card-title">${date}</div>
                    <div class="card-title">$ ${price}</div>
                <p class="card-text">
                <div>${authors[0].firstName} ${authors[0].lastName}</div>
                </p>
            </div>
 </div>`)
};