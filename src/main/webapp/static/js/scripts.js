var counter =0;
var counterQuestion=0;

function addAnswer(node) {
	counter++;
	var nameAnswer = node.parentNode.getAttribute("id") + "js" + counter;
	
	var answer = document.createElement("div");
	answer.setAttribute("class", "col-xs-4");
		
	var input = document.createElement("input");
	input.setAttribute("type","text");
	input.setAttribute("class","form-control");
	input.setAttribute("id",nameAnswer);
	input.setAttribute("name",nameAnswer);
	input.setAttribute("required","");
	answer.appendChild(input);
	
	var radio = document.createElement("div");
	radio.setAttribute("class", "radio");
	
	
	var labelT = document.createElement("label");
	
	labelT.innerHTML = "<input type=\"radio\" name=\"" + 
	nameAnswer+ "\" value=\"true\">True";
		
	var labelF = document.createElement("label");
	labelF.innerHTML = "<input type=\"radio\" name=\"" + 
	nameAnswer+ "\" value=\"false\" checked>False";
		
	var labelA = document.createElement("label");
	var a = document.createElement("a");
	a.setAttribute("onclick","return deleteAnswer(this)");
	a.innerHTML = "Delete";
	labelA.appendChild(a);
	
	radio.appendChild(labelT);
	radio.appendChild(labelF);
	radio.appendChild(labelA);
	
	answer.appendChild(radio);
		
	node.parentNode.children[2].appendChild(answer);
	
	updateParameters();
	return false;
}


function deleteAnswer(node) {
	
	var contDiv = node.parentNode.parentNode.parentNode;
	contDiv.parentNode.removeChild(contDiv);
	return false;
}

function addQuestion(node) {
	var k = 0;
	
	var div = document.createElement("div");
	
	counterQuestion++;
	
	var nameQuestion =  "question[" +"js" + counterQuestion +"]";
	div.setAttribute('id', nameQuestion);
	
	var textArea = document.createElement("textarea");
	textArea.setAttribute("class", "form-control");
	textArea.setAttribute("id", "textQuestion[" + nameQuestion + "]");
	textArea.setAttribute("name", "textQuestion[" + nameQuestion + "]");
	textArea.setAttribute("rows", "2");
	
	div.appendChild(textArea);
	div.appendChild(document.createElement("br"));
	
	var answerContainer = document.createElement("div");
	answerContainer.setAttribute("class", "row");
	answerContainer.setAttribute("id", "answerContainer");
	
	for(i = 0;i < 3; i++){
		var answer = document.createElement("div");
		answer.setAttribute("class", "col-xs-4");
		
		var input = document.createElement("input");
		input.setAttribute("type","text");
		input.setAttribute("class","form-control");
		input.setAttribute("id",nameQuestion+i);
		input.setAttribute("name",nameQuestion+i);
		input.setAttribute("required","");
		answer.appendChild(input);
		
		var radio = document.createElement("div");
		radio.setAttribute("class", "radio");

		var labelT = document.createElement("label");
		labelT.innerHTML = "<input type=\"radio\" name=\"" + 
		nameQuestion+i + "\" value=\"true\">True";
		
		var labelF = document.createElement("label");
		labelF.innerHTML = "<input type=\"radio\" name=\"" + 
		nameQuestion+i + "\" value=\"false\" checked>False";
		
		radio.appendChild(labelT);
		radio.appendChild(labelF);
		
		if(i>=2) {
			var labelA = document.createElement("label");
			var a = document.createElement("a");
			a.setAttribute("onclick","return deleteAnswer(this)");
			a.innerHTML = "Delete";
			labelA.appendChild(a);
			radio.appendChild(labelA);
		}
		
		answer.appendChild(radio);
		
		answerContainer.appendChild(answer);
	}
	div.appendChild(answerContainer);
	
	var button = document.createElement("button");
	button.setAttribute("type","submit");
	button.setAttribute("class","btn btn-default");
	button.setAttribute("onclick","return addAnswer(this)");
	button.innerHTML = "Add Answer";
	
	div.appendChild(button);
	
	var buttonDel = document.createElement("button");
	buttonDel.setAttribute("type","submit");
	buttonDel.setAttribute("class","btn btn-danger");
	buttonDel.setAttribute("onclick","return deleteQuestion(this)");
	buttonDel.innerHTML = "Delete Question";
	
	div.appendChild(buttonDel);
	
	node.parentNode.children[0].appendChild(div);

	updateParameters();
	return false;
}

function deleteQuestion(node){
	var contDiv = node.parentNode;
	contDiv.parentNode.removeChild(contDiv);
	return false;
}

function updateParameters(){
	
}