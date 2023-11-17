// 취향도 표시하는 함수
function drawStars(score, container) {
    var blackStars = '<span class="star">❤️</span>'; // 까만별 문자
    var whiteStars = '<span class="star">🤍</span>'; // 하얀별 문자

    // 까만별 그리기
    for (var i = 0; i < score; i++) {
        container.append(blackStars);
    }

    // 하얀별 그리기
    for (var i = score; i < 5; i++) {
        container.append(whiteStars);
    }
}
$(function () {

    // 각 리뷰에 대한 평점 표시
    $('.list-score').each(function() {
        var score = parseInt($(this).closest('li').find('.list-score-value').val()); // 점수 가져오기
        drawStars(score, $(this));
    });

    // li 요소 클릭 시 독후감 상세 화면으로 이동
    $(document).on('click', '.review-item', function() {
        var reviewId = $(this).data('review-id');   // 독후감 PK
        window.location.href = '/review/' + reviewId;
    });

    // 도서 검색 input에 입력 시 이벤트 발생
    $("#searchReview").keypress(function(){
        // 입력값 가져오기
        var searchReview = $(this).val();

        $.ajax({
            type:'POST',
            url:'/review/search',
            data:JSON.stringify({"searchReview": searchReview}),
            contentType:'application/json; charset=UTF-8',
            success:function (res) {
                console.log(res);
                $('.review-contents-list > ul').empty(); // 독후감 목록 초기화

                var bookReviewList = res.bookReviewList; // 검색된 독후감 목록
                if (bookReviewList.length > 0 ) {
                    bookReviewList.forEach(function (review) {

                        var listItem = $('<li>').addClass('review-item').attr('data-review-id', review.reviewId);

                        $('<input>').attr('type', 'hidden').addClass('list-score-value').attr('value', review.score).appendTo(listItem);

                        $('<div>').addClass('review-book-img').append($('<img>').attr('src', review.book.cover)).appendTo(listItem);

                        var bookContents = $('<div>').addClass('review-book-contents');
                        $('<h2>').text(review.book.title).appendTo(bookContents);
                        $('<p>').text(review.shortReview).appendTo(bookContents);
                        bookContents.appendTo(listItem);

                        var reviewInfo = $('<div>').addClass('review-book-info');
                        $('<p>').text(review.regDt).appendTo(reviewInfo);
                        $('<p>').addClass('list-score').appendTo(reviewInfo);

                        if (review.openYn) {
                            $('<p>').addClass('review-openYn').text('공개').appendTo(reviewInfo);
                        } else {
                            $('<p>').addClass('review-openYn').text('비공개').appendTo(reviewInfo);
                        }

                        reviewInfo.appendTo(listItem);
                        $('.review-contents-list > ul').append(listItem);
                    });
                } else {
                    $('.review-contents-list > ul').append($('<p>').addClass('review-no-contents').text('검색된 도서의 독후감이 없습니다.'));
                }
            }
        })
    });

})