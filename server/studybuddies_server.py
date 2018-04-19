from flask import Flask, jsonify, request
import json
import mysql.connector
import hashlib
import datetime

cnx = mysql.connector.connect(user='root', database='studybuddies')
cursor = cnx.cursor()

app = Flask(__name__)

@app.route('/student/', methods=['POST'])
def editProfile():
    cnx = mysql.connector.connect(user='root', database='studybuddies')
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
    cnx = mysql.connector.connect(user='root', database='studybuddies')
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
    cnx = mysql.connector.connect(user='root', database='studybuddies')
    cursor = cnx.cursor()

    title = request.form['title']
    description = request.form['description']
    creator = request.form['creator']
    courseid = request.form['courseid']

    try:
        meetingtimestampFrom = datetime.datetime.strptime(request.form['meetingTimeTo'],'%Y-%m-%d %H:%M:%S')
    except ValueError as verr:
        return jsonify({'success':1,'message':'Datumformat wurde nicht eingehalten'});

    try:
        meetingtimestampTo = datetime.datetime.strptime(request.form['meetingTimeFrom'],'%Y-%m-%d %H:%M:%S')
    except ValueError as verr:
        return jsonify({'success':1,'message':'Datumformat wurde nicht eingehalten'});

    meetingpointid = request.form['meetingpointid']
    maxstudentcount = request.form['maxstudentcount']

    try:
        cursor.execute("INSERT INTO Learngroup VALUES ('','%s','%s','%s','%s','%s','%s','%s','%s')" % (creator,courseid,meetingtimestampFrom,meetingtimestampTo,meetingpointid,title,description,maxstudentcount))
        cursor.execute("INSERT INTO StudentLearngroup VALUES ('%s','%s')" % (creator,cursor.lastrowid))
        cnx.commit()
    except mysql.connector.Error as e:
        return jsonify({'success':1,'message':'Lerngruppe konnte nicht erstellt werden','error':str(e)});
    return jsonify({'success':0,'message':'Neue Lerngruppe wurde erstellt'})

@app.route('/session/', methods=['POST'])
def editSession():

    cnx = mysql.connector.connect(user='root', database='studybuddies')
    cursor = cnx.cursor()

    courseid = request.form['courseid']
    action = request.form['action']

    try:
        meetingtimestampFrom = datetime.datetime.strptime(request.form['meetingTimeFrom'],'%Y-%m-%d %H:%M:%S')
    except ValueError as verr:
        return jsonify({'success':1,'message':'Datumformat wurde nicht eingehalten'});
    try:
        meetingtimestampTo = datetime.datetime.strptime(request.form['meetingTimeTo'],'%Y-%m-%d %H:%M:%S')
    except ValueError as verr:
        return jsonify({'success':1,'message':'Datumformat wurde nicht eingehalten'});

    try:
        cursor.execute("UPDATE Learngroup SET MeetingtimeFrom = '%s', MeetingtimeTo = '%s' WHERE LID = '%s'" % (meetingtimestampFrom,meetingtimestampTo,courseid))
        cnx.commit()
    except mysql.connector.Error as e:
        return jsonify({'success':1,'message':'Session konnte nicht übernommen werden','error':str(e)});
    return jsonify({'success':0,'message':'Session übernommen','action':action})

@app.route('/learngroup/join/', methods=['POST'])
def joinLearngroup():
    cnx = mysql.connector.connect(user='root', database='studybuddies')
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

@app.route('/learngroup/leave/', methods=['POST'])
def leaveLearngroup():
    cnx = mysql.connector.connect(user='root', database='studybuddies')
    cursor = cnx.cursor()

    lid = request.form['lid']
    studentname = request.form['studentname']

    try:
        cursor.execute("DELETE FROM StudentLearngroup WHERE Fullname = '%s' AND LID = '%s'" % (studentname,lid))
        cnx.commit()
    except mysql.connector.Error as e:
        return jsonify({'success':1,'message':'Konnte Lerngruppe nicht verlassen'});
    return jsonify({'success':0,'message':'Lerngruppe erfolgreich verlassen'})

@app.route('/learngroup/edit/', methods=['POST'])
def editLearngroup():
    cnx = mysql.connector.connect(user='web', database='studybuddies')
    cursor = cnx.cursor()

    lid = request.form['lid']
    title = request.form['title']
    description = request.form['description']
    cid = request.form['cid']
    mpid = request.form['mpid']

    try:
        cursor.execute("UPDATE Learngroup SET Title = '%s',Description='%s',CID='%s',MeetingPointID='%s' WHERE LID = '%s';" % (title,description,cid,mpid,lid))
        cnx.commit()
    except mysql.connector.Error as e:
        return jsonify({'success':1,'message':'Konnte Lerngruppe nicht editieren'});
    return jsonify({'success':0,'message':'Lerngruppe erfolgreich editiert'})

@app.route('/student/', methods=['GET'])
def getProfile():
    cnx = mysql.connector.connect(user='root', database='studybuddies')
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
    cnx = mysql.connector.connect(user='root', database='studybuddies')
    cursor = cnx.cursor()

    courseid = request.values.get("courseid");
    print("ok")
    if courseid != None:
        query = "SELECT l.LID,l.Creator,DATE_FORMAT(l.MeetingtimeFrom, '%%d.%%m.%%Y %%H:%%i'),DATE_FORMAT(l.MeetingtimeTo, '%%d.%%m.%%Y %%H:%%i'),mp.Name,l.Title,l.Description,l.MaxStudentCount,c.Name,cp.Name,Count(sl.LID) FROM Learngroup l INNER JOIN MeetingPoint mp ON l.MeetingPointID = mp.MPID INNER JOIN Course c ON l.CID = c.CID INNER JOIN Campus cp ON mp.CPID = cp.CPID INNER JOIN StudentLearngroup sl ON l.LID = sl.LID AND l.CID = %s GROUP BY sl.LID ORDER BY l.MeetingtimeFrom" % courseid;
    else:
        query = "SELECT l.LID,l.Creator,DATE_FORMAT(l.MeetingtimeFrom, '%d.%m.%Y %H:%i'),DATE_FORMAT(l.MeetingtimeTo, '%d.%m.%Y %H:%i'),mp.Name,l.Title,l.Description,l.MaxStudentCount,c.Name,cp.Name,Count(sl.LID) FROM Learngroup l INNER JOIN MeetingPoint mp ON l.MeetingPointID = mp.MPID INNER JOIN Course c ON l.CID = c.CID INNER JOIN Campus cp ON mp.CPID = cp.CPID INNER JOIN StudentLearngroup sl ON l.LID = sl.LID GROUP BY sl.LID ORDER BY l.MeetingtimeFrom";
    cursor.execute(query)
    response = []
    for (LID,Creator,MeetingtimeFrom,MeetingtimeTo,location,Title,Description,MaxStudentCount,CourseName,Campus,ParticipantsCount) in cursor:
        jsonStr = {
            'LID' : LID,
            'Creator' : Creator,
            'MeetingtimeFrom' : MeetingtimeFrom,
            'MeetingtimeTo' : MeetingtimeTo,
            'Location' : location,
            'Title' : Title,
            'Description' : Description,
            'MaxStudentCount' : MaxStudentCount,
            'Course': CourseName,
            'Campus': Campus,
            'StudentCount': ParticipantsCount
        }
        print(LID)
        response.append(jsonStr)

    return jsonify(response)


@app.route('/learngroup/<studentname>', methods=['GET'])
def getLearngroupsForStudent(studentname):
    cnx = mysql.connector.connect(user='root', database='studybuddies')
    cursor = cnx.cursor()

    cursor.execute("SELECT l.LID,l.Creator,DATE_FORMAT(l.MeetingtimeFrom, '%%d.%%m.%%Y %%H:%%i'),DATE_FORMAT(l.MeetingtimeTo, '%%d.%%m.%%Y %%H:%%i'),mp.Name,l.Title,l.Description,l.MaxStudentCount,c.Name,cp.Name,Count(sl.LID) FROM Learngroup l INNER JOIN MeetingPoint mp ON l.MeetingPointID = mp.MPID INNER JOIN Course c ON l.CID = c.CID INNER JOIN Campus cp ON mp.CPID = cp.CPID INNER JOIN StudentLearngroup sl ON l.LID = sl.LID WHERE sl.LID IN (SELECT LID FROM StudentLearngroup sl WHERE sl.Fullname = '%s') GROUP BY sl.LID ORDER BY l.MeetingtimeFrom;" % (studentname))

    response = []
    for (LID,Creator,MeetingtimeFrom,MeetingtimeTo,location,Title,Description,MaxStudentCount,CourseName,Campus,ParticipantsCount) in cursor:
        jsonStr = {
            'LID' : LID,
            'Creator' : Creator,
            'MeetingtimeFrom' : MeetingtimeFrom,
            'MeetingtimeTo' : MeetingtimeTo,
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

@app.route('/learngroup/<lid>/meetingpoints/', methods=['GET'])
def getMeetingpoint(lid):
    cnx = mysql.connector.connect(user='root', database='studybuddies')
    cursor = cnx.cursor()

    meetingpoints = []

    cursor.execute("SELECT Campus.CPID, MPID, Campus.Name AS Campus, MeetingPoint.Name AS MeetingPoint FROM MeetingPoint,Campus WHERE Campus.CPID = MeetingPoint.CPID;")
    for (CPID,MPID,Campus,MeetingPoint) in cursor:
        jsonStr = {
            'CPID' : CPID,
            'MPID' : MPID,
            'Campus' : Campus,
            'MeetingPoint' : MeetingPoint
        }
        meetingpoints.append(jsonStr)

    cursor.execute("SELECT Campus.CPID, MPID FROM MeetingPoint,Campus WHERE MPID = (SELECT MeetingPointID FROM Learngroup WHERE LID = '%s') AND Campus.CPID = MeetingPoint.CPID;" % (lid))
    response = []

    for (CPID, MPID) in cursor:
        jsonStr = {
            'CPID' : CPID,
            'MPID' : MPID,
            'Campus' : Campus,
            'MeetingPoint' : MeetingPoint,
            'MeetingPoints' : meetingpoints
        }
        response.append(jsonStr)


    return jsonify(response)

cursor.close()
cnx.close()

if __name__ == "__main__":
    app.run(
    host="0.0.0.0",
    port=5000
)
