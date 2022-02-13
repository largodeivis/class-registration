# Course Registration
Course Registration Java SpringBoot API.

**Prerequisites**
* Docker v20.10.10 build b485636

**Running Project**
1. Clone from GitHub.
2. Build Docker image
3. Run docker image

```
git clone git@github.com:largodeivis/class-registration.git
docker build -t class-reg .
docker run -p 8080:8080 class-reg
```

## Calling the API
### Create Student

_Post_ Request to _/students/_ with a request body.

```
localhost:8080/students
```

**Request Body**
```
{
"name": "Freddy"
}
```

![POST Students Image](img/POSTStudents.png)


### Create Professor

_Post_ Request to _/professors/_ with a request body.

```
localhost:8080/professors
```

**Request Body**
```
{
"name": "Scooby Doo"
}
```

![POST Professors Image](img/POSTProfessors.png)

### Create Courses

_Post_ Request to _/courses/_ with a request body. CourseNumbers must be unique.

```
localhost:8080/courses
```

**Request Body**
```
{
	"courseName":"Cyber Crime",
	"courseNumber":"CYB102"
}
```

![POST Courses Image](img/POSTCourses.png)


### Assign a Professor to a Course

_Post_ Request to _/courses/**{courseId}**/professor/**{professorId}**_. 

```
localhost:8080/courses/1/professor/1
```

![POST Assign Professor Image](img/POSTAssignProfessor.png)

**Response**

![Assign Professor Response Image](img/RESPONSEAssignProfessor.png)


### Register a Student to a Course

_Post_ Request to _/courses/**{courseId}**/student/**{studentId}**_.

```
localhost:8080/courses/1/student/1
```

![POST Register Student Image](img/POSTRegisterStudent.png)

**Response**

![Register Student Response Image](img/RESPONSERegisterStudent.png)


## Load Test Results
* Load tests have been performed using [loadtest](https://github.com/alexfernandez/loadtest).

### Create Professors
* **localhost:8080/professors**
```
{
totalRequests: 5996,
totalErrors: 0,
totalTimeSeconds: 30.0365671,
rps: 200,
meanLatencyMs: 12.4,
maxLatencyMs: 79,
minLatencyMs: 4,
percentiles: { '50': 11, '90': 18, '95': 21, '99': 30 },
errorCodes: {},
instanceIndex: 0
}
```

### Create Students
* **localhost:8080/students**
```
{
totalRequests: 5998,
totalErrors: 0,
totalTimeSeconds: 30.029641599999998,
rps: 200,
meanLatencyMs: 13.8,
maxLatencyMs: 158,
minLatencyMs: 4,
percentiles: { '50': 11, '90': 21, '95': 26, '99': 40 },
errorCodes: {},
instanceIndex: 0
}
```


### Create Courses
* A [bug](https://github.com/alexfernandez/loadtest/issues/198) in [loadtest](https://github.com/alexfernandez/loadtest) prevents me from firing off too many requests with unique parameters.
* **localhost:8080/courses**

```
{
totalRequests: 250,
totalErrors: 0,
totalTimeSeconds: 1.2510715000000001,
rps: 200,
meanLatencyMs: 1039.2,
maxLatencyMs: 1205,
minLatencyMs: 873,
percentiles: { '50': 1045, '90': 1155, '95': 1181, '99': 1202 },
errorCodes: {},
instanceIndex: 0
}
```


### Assign Professor to Course
* A [bug](https://github.com/alexfernandez/loadtest/issues/198) in [loadtest](https://github.com/alexfernandez/loadtest) prevents me from firing off too many requests with unique parameters.
* **localhost:8080/courses/{course_id}/professor/{professor_id}**
```
{
totalRequests: 250,
totalErrors: 0,
totalTimeSeconds: 0.7444849,
rps: 336,
meanLatencyMs: 492.7,
maxLatencyMs: 688,
minLatencyMs: 245,
percentiles: { '50': 506, '90': 653, '95': 679, '99': 686 },
errorCodes: {},
instanceIndex: 0
}
```

### Register Student to Course
* A [bug](https://github.com/alexfernandez/loadtest/issues/198) in [loadtest](https://github.com/alexfernandez/loadtest) prevents me from firing off too many requests with unique parameters.
* **localhost:8080/courses/{course_id}/student/{student_id}**
```
{
totalRequests: 250,
totalErrors: 0,
totalTimeSeconds: 0.7035143,
rps: 355,
meanLatencyMs: 439.4,
maxLatencyMs: 650,
minLatencyMs: 244,
percentiles: { '50': 462, '90': 542, '95': 568, '99': 642 },
errorCodes: {},
instanceIndex: 0
}
```

