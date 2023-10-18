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
    $('.review-item').click(function() {
        var reviewId = $(this).data('review-id');   // 독후감 PK
        window.location.href = '/review/' + reviewId;
    });

})