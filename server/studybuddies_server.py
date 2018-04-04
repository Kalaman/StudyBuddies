from flask import Flask, jsonify, request
import json
import mysql.connector
import hashlib
import datetime

cnx = mysql.connector.connect(user='web', database='studybuddies')
cursor = cnx.cursor()

app = Flask(__name__)

@app.route('/student/', methods=['POST'])
def editProfile():
    cnx = mysql.connector.connect(user='web', database='studybuddies')
    cursor = cnx.cursor()

    studentname = request.form['studentname']
    semester = request.form['semester']
    studyProgramID = request.form['studyprogramid']
    description = request.form['description']
    phone = request.form['phone']
    print (request.form)
    try:
        cursor.execute("UPDATE Student SET Fullname = '%s', Semester = '%s', StudyProgramID = '%s', Description = '%s', Phone = '%s' WHERE Fullname = '%s';" % (studentname,semester,studyProgramID,description,phone,studentname))
        cnx.commit()
    except mysql.connector.IntegrityError as err:
        return jsonify({'success':1,'message':'Unbekannter fehler'});
    return jsonify({'success':0,'message':'Profil erfolgreich aktualisiert'})

@app.route('/register/', methods=['POST'])
def registerStudent():
    cnx = mysql.connector.connect(user='web', database='studybuddies')
    cursor = cnx.cursor()

    studentname = request.form['studentname']
    semester = request.form['semester']
    studentprogram = request.form['studentprogram']
    print (request.form)
    print ("INSERT INTO Student VALUES ('%s','%s','%s','','')" % (studentname,semester,studentprogram))
    try:
        cursor.execute("INSERT INTO Student VALUES ('%s','%s','%s','','');" % (studentname,semester,studentprogram))
        cnx.commit()
    except mysql.connector.IntegrityError as err:
        if err.errno == 1062:
            return jsonify({'success':1,'message':'Student existiert bereits'});
        else:
            return jsonify({'success':1,'message':'Unbekannter fehler'});
    return jsonify({'success':0,'message':'Student erfolgreich registriert'})


@app.route('/learngroup/', methods=['POST'])
def createLearngroup():
    cnx = mysql.connector.connect(user='web', database='studybuddies')
    cursor = cnx.cursor()

    title = request.form['title']
    description = request.form['description']
    creator = request.form['creator']
    courseid = request.form['courseid']

    try:
        meetingtimestamp = datetime.datetime.strptime(request.form['meetingtime'],'%Y-%m-%d %H:%M:%S')
    except ValueError as verr:
        return jsonify({'success':1,'message':'Datumformat wurde nicht eingehalten'});

    meetingpointid = request.form['meetingpointid']
    maxstudentcount = request.form['maxstudentcount']

    try:
        cursor.execute("INSERT INTO Learngroup VALUES ('','%s','%s','%s','%s','%s','%s','%s')" % (creator,courseid,meetingtimestamp,meetingpointid,title,description,maxstudentcount))
        cursor.execute("INSERT INTO StudentLearngroup VALUES ('%s','%s')" % (creator,cursor.lastrowid))
        cnx.commit()
    except mysql.connector.Error as e:
        return jsonify({'success':1,'message':'Lerngruppe konnte nicht erstellt werden','error':str(e)});
    return jsonify({'success':0,'message':'Neue Lerngruppe wurde erstellt'})


@app.route('/learngroup/join/', methods=['POST'])
def joinLearngroup():
    cnx = mysql.connector.connect(user='web', database='studybuddies')
    cursor = cnx.cursor()

    lid = request.form['lid']
    studentname = request.form['studentname']

    try:
        cursor.execute("INSERT INTO StudentLearngroup VALUES ('%s','%s')" % (studentname,lid))
        cnx.commit()
    except mysql.connector.Error as e:
        if e.sqlstate == '45000':
            return jsonify({'success':1,'message':'Maximale Studentenanzahl ist erreicht'});
        elif e.sqlstate == '23000':
            return jsonify({'success':1,'message':'Student bereits in Lerngruppe'});
        else:
            return jsonify({'success':1,'message':'Konnte Lerngruppe nicht beitreten'});
    return jsonify({'success':0,'message':'Lerngruppe beigetreten'})

@app.route('/student/', methods=['GET'])
def getProfile():
    cnx = mysql.connector.connect(user='web', database='studybuddies')
    cursor = cnx.cursor()

    studentname = request.values.get("name");

    if studentname != None:
        query = "SELECT Fullname, Semester, StudyProgramID, Description, Phone FROM Student WHERE %s = Fullname" % studentname;
    else:
        query = "SELECT Fullname, Semester, StudyProgramID, Description, Phone FROM Student";
    cursor.execute(query)
    response = []
    for (Fullname,Semester,StudyProgramID,Description,Phone) in cursor:
        jsonStr = {
            'Student' : Fullname,
            'Semester' : Semester,
            'StudyProgramID' : StudyProgramID,
            'Description' : Description,
            'Phone' : Phone,
        }
        response.append(jsonStr)

    return jsonify(response)

@app.route('/learngroup/', methods=['GET'])
def getLearngroups():
    cnx = mysql.connector.connect(user='web', database='studybuddies')
    cursor = cnx.cursor()

    courseid = request.values.get("courseid");

    if courseid != None:
        query = "SELECT l.LID,l.Creator,DATE_FORMAT(l.Meetingtime, '%%d.%%l.%%Y %%H:%%i'),mp.Name,l.Title,l.Description,l.MaxStudentCount,c.Name,cp.Name,Count(sl.LID) FROM Learngroup l INNER JOIN MeetingPoint mp ON l.MeetingPointID = mp.MPID INNER JOIN Course c ON l.CID = c.CID INNER JOIN Campus cp ON mp.CPID = cp.CPID INNER JOIN StudentLearngroup sl ON l.LID = sl.LID WHERE l.Meetingtime >= NOW() AND l.CID = %s GROUP BY sl.LID ORDER BY l.Meetingtime" % courseid;
    else:
        query = "SELECT l.LID,l.Creator,DATE_FORMAT(l.Meetingtime, '%d.%l.%Y %H:%i'),mp.Name,l.Title,l.Description,l.MaxStudentCount,c.Name,cp.Name,Count(sl.LID) FROM Learngroup l INNER JOIN MeetingPoint mp ON l.MeetingPointID = mp.MPID INNER JOIN Course c ON l.CID = c.CID INNER JOIN Campus cp ON mp.CPID = cp.CPID INNER JOIN StudentLearngroup sl ON l.LID = sl.LID WHERE l.Meetingtime >= NOW() GROUP BY sl.LID ORDER BY l.Meetingtime";
    cursor.execute(query)
    response = []
    for (LID,Creator,Meetingtime,location,Title,Description,MaxStudentCount,CourseName,Campus,ParticipantsCount) in cursor:
        jsonStr = {
            'LID' : LID,
            'Creator' : Creator,
            'Meetingtime' : Meetingtime,
            'Location' : location,
            'Title' : Title,
            'Description' : Description,
            'MaxStudentCount' : MaxStudentCount,
            'Course': CourseName,
            'Campus': Campus,
            'StudentCount': ParticipantsCount
        }
        response.append(jsonStr)

    return jsonify(response)


@app.route('/learngroup/<studentname>', methods=['GET'])
def getLearngroupsForStudent(studentname):
    cnx = mysql.connector.connect(user='web', database='studybuddies')
    cursor = cnx.cursor()

    cursor.execute("SELECT l.LID,l.Creator,DATE_FORMAT(l.Meetingtime, '%%d.%%l.%%Y %%H:%%i'),mp.Name,l.Title,l.Description,l.MaxStudentCount,c.Name,cp.Name,Count(sl.LID) FROM Learngroup l INNER JOIN MeetingPoint mp ON l.MeetingPointID = mp.MPID INNER JOIN Course c ON l.CID = c.CID INNER JOIN Campus cp ON mp.CPID = cp.CPID INNER JOIN StudentLearngroup sl ON l.LID = sl.LID WHERE sl.LID IN (SELECT LID FROM StudentLearngroup sl WHERE sl.Fullname = '%s') GROUP BY sl.LID ORDER BY l.Meetingtime;" % (studentname))

    response = []
    for (LID,Creator,Meetingtime,location,Title,Description,MaxStudentCount,CourseName,Campus,ParticipantsCount) in cursor:
        jsonStr = {
            'LID' : LID,
            'Creator' : Creator,
            'Meetingtime' : Meetingtime,
            'Location' : location,
            'Title' : Title,
            'Description' : Description,
            'MaxStudentCount' : MaxStudentCount,
            'Course': CourseName,
            'Campus': Campus,
            'StudentCount': ParticipantsCount
        }
        response.append(jsonStr)

    return jsonify(response)


cursor.close()
cnx.close()

if __name__ == "__main__":
    app.run(host='0.0.0.0')
