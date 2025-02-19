<?php
session_start();

$db = new SQLite3('users.sqlite', SQLITE3_OPEN_READWRITE | SQLITE3_OPEN_CREATE);
$db->exec("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT UNIQUE, password TEXT, reset_token TEXT)");

function generatePasswordResetToken($user)
{
    global $db;
    $token = md5(mt_rand(1, 100) . $user . time() . session_id());
    $p = $db->prepare('UPDATE users SET reset_token = :token WHERE name = :user');
    $p->bindValue(':user', $user, SQLITE3_TEXT);
    $p->bindValue(':token', $token, SQLITE3_TEXT);
    $p->execute();
    echo "Password reset token: $token\n";
}

function changePassword($token, $newPassword)
{
    global $db;
    $p = $db->prepare('SELECT id FROM users WHERE reset_token = :token');
    $p->bindValue(':token', $token, SQLITE3_TEXT);
    $res = $p->execute()->fetchArray(1);
    
    if (strlen($token) == 32 && $res) 
    {
        $p = $db->prepare('UPDATE users SET password = :password WHERE id = :id');
        $p->bindValue(':password', $newPassword, SQLITE3_TEXT);
        $p->bindValue(':id', $res['id'], SQLITE3_INTEGER);
        $p->execute();
        die("Password changed!\n");
    }
    http_response_code(403);
    die("Invalid reset token!\n");
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (isset($_POST['user'])) {
        generatePasswordResetToken($_POST['user']);
    } elseif (isset($_POST['token'], $_POST['newPassword'])) {
        changePassword($_POST['token'], $_POST['newPassword']);
    } else {
        http_response_code(400);
        die("Invalid request!\n");
    }
}
?>
