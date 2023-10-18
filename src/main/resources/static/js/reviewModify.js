
// 취향도 표시
function drawStars(score, container) {
    var blackStars = '<label><input type="checkbox" checked><span>❤️</span></label>'; // 까만별 문자
    var whiteStars = '<label><input type="checkbox" ><span>🤍</span></label>'; // 하얀별 문자

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

    // 현재의 공개 상태 표시
    var prevOpenYn = $('#prevOpenYn').val();
    var openYn = $('#openYn');
    if (prevOpenYn) {
        openYn.addClass('list-open-y');
        openYn.text("공개");
    } else {
        openYn.addClass('list-open-n');
        openYn.text("비공개");
    }

    // 공개/비공개 toggle
    openYn.click(function () {
        var openYnText = openYn.text();
        openYn.removeClass();
        if (openYnText === '공개') {
            openYn.addClass('list-open-n');
            openYn.text("비공개");
        } else {
            openYn.addClass('list-open-y');
            openYn.text("공개");
        }
    });

    // 현재의 취향도 표시
    var score = parseInt($('.score').val()); // 점수 가져오기
    drawStars(score, $('.heart'));

    // 취향도 선택
    $('.heart label').click(function() {
        // 현재 클릭한 레이블을 포함한 모든 레이블
        var labels = $(this).prevAll('label').addBack();

        // 클릭한 레이블부터 가장 왼쪽 레이블까지의 텍스트를 ❤️로 변경
        labels.find('span').text('❤️');

        // 클릭한 레이블 다음의 레이블의 텍스트를 🤍로 변경
        labels.nextAll('label').find('span').text('🤍');
    });

    // 독후감 저장
    $('#book-save-btn').click(function () {

        // 취향도 구하기
       var checkedCheckboxes = $(".heart input[type='checkbox']:checked");
       var heartCnt = checkedCheckboxes.length;

       // validate
        var isbn = $('#searchBook').val();
        var title = $('#title').val().trim();
        var author = $('#author').text().trim();
        var publisher = $('#publisher').text().trim();
        var openYn = $('input[name="openYn"]:checked').val();
        var cover = $('#cover').attr('src');
        var shortReview = $('#shortReview').val().trim();
        var longReview = $('#longReview').val().trim();

        if (!isbn) {
            alert('도서를 선택해주세요.');
        } else if (!shortReview){
            alert('한줄평을 작성해주세요.');
        } else if (shortReview.length > 130) {
            alert('한줄평을 130글자 미만으로 작성해주세요'); // Todo 실시간 info
        } else {
           var requestParam = {
               'isbn' : isbn,
               'title' : title,
               'author' : author,
               'publisher' : publisher,
               'openYn' : openYn,
               'cover' : cover,
               'score' : heartCnt,
               'shortReview' : shortReview,
               'longReview' : longReview
           }

            // 저장
            $.ajax({
                type:'POST',
                url:'/review/write',
                data:JSON.stringify(requestParam),
                contentType: 'application/json',
                success:function (res) {
                    location.href="/";
                },
                error:function () {
                    alert('저장에 실패했습니다.');
                }
            })
        }
    });
})