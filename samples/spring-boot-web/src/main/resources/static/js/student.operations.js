function loadAllStudentDetails() {
  
  console.log('Loading student details...');
  
  $.getJSON("/students")
    .done(function(students) {
    
    console.log('Received students: %s', JSON.stringify(students));
    
    displayStudents(students);
    
  }).fail(function(error) {
    
    console.error('### fail !!!' + JSON.stringify(error));
    
  });
}

function loadSingleStudentDetails() {
  
  var studentId = window.location.pathname.substring(window.location.pathname.lastIndexOf("/") + 1);
  
  console.log('Loading student details for %s...', studentId);
  
  $.getJSON('/students/' + studentId)
    .done(function(student) {
    
    console.log('Received students: %s', JSON.stringify(student));
    
    displayStudentDetails(student);
    
  }).fail(function(error) {
    
    console.error('### fail !!!' + JSON.stringify(error));
    
  });
}

function displayStudents(students) {
  
  var tableRows = document.createDocumentFragment();
  
  var tr = document.createElement("tr");
  tr.appendChild(createElementWithContent("td", "<b>ID</b>"));
  tr.appendChild(createElementWithContent("td", "<b>First Name</b>"));
  tr.appendChild(createElementWithContent("td", "<b>Last Name</b>"));
  tr.appendChild(createElementWithContent("td", "<b>Age</b>"));
  
  tableRows.appendChild(tr);
  
  $.each(students, function(index, student) {
    
    tr = document.createElement("tr");

    var studentId = "<a href='/students/" + student.studentId + "' target='_blank'>" + student.studentId + "</a>";
    tr.appendChild(createElementWithContent("td", studentId));
    tr.appendChild(createElementWithContent("td", student.firstName));
    tr.appendChild(createElementWithContent("td", student.lastName));
    tr.appendChild(createElementWithContent("td", student.age));
    
    tableRows.appendChild(tr);
  });
  
  var table = document.createElement("table");

  table.style.border = "1px solid black";
  
  table.appendChild(tableRows);

  document.getElementById("studentDetails").appendChild(table);  
}

function displayStudentDetails(student) {

  $('#txtStudentId').val(student.studentId);
  $('#txtFirstName').val(student.firstName);
  $('#txtLastName').val(student.lastName);
  $('#txtAge').val(student.age);
}

function createElementWithContent(elementName, content) {
  
  var element = document.createElement(elementName);
  
  element.innerHTML = content;
  
  element.style.border = "1px solid black";
  
  return element;
}