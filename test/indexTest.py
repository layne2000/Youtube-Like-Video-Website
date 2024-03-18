import pymysql
import pymysql.cursors
import random

connection = pymysql.connect(host='127.0.0.1',
                             user='root',
                             password='mysqlroot123',
                             database='miniYoutube',
                             cursorclass=pymysql.cursors.DictCursor)

try:
    with connection.cursor() as cursor:
        # sql = "INSERT INTO t_video_view (videoId) VALUES (%s)"
        # sql = "INSERT INTO t_live_comment (videoId, userId, appearingTime) VALUES (%s, %s, %s)"
        sql = "INSERT INTO t_video_comment (videoId, userId, content) VALUES (%s, %s, %s)"
        sql2 = "INSERT INTO t_video_comment (videoId, userId, rootCommentId, content) VALUES (%s, %s, %s, %s)"

        large_data_list = []
        large_data_list2 = []
        for i in range(1, 1000001):
            # large_data_list.append((random.randint(1, 1000))
            # large_data_list.append((random.randint(1, 1000), 4, str(random.randint(1, 1000))))
            large_data_list.append((random.randint(1, 1000), 4, "this is a test"))
            large_data_list2.append((random.randint(1, 1000), 4, random.randint(1, 1000), "this is a test"))
        
        cursor.executemany(sql, large_data_list)
        cursor.executemany(sql2, large_data_list2)
        
        connection.commit()
       
finally:
    connection.close()
