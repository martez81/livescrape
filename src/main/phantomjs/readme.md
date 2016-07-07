

// Change click event handler
document.addEventListener("click",handler,true);

function handler(e){
    e.stopPropagation();
    e.preventDefault();
    console.log(e); // x and y will have absolute click position
}

// Capture element
var target = $(document.elementFromPoint(745, 206));


$('tr#g_1_l8i1SnM8').find('td.score').eq(0).html();