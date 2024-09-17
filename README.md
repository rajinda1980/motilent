# Assumptions

1. No spaces are allowed in the file path argument
2. The file path length cannot exceed 200 characters
3. The application expects the reportUID and studyInstanceUID fields to be directly part of the notificationContent object, rather than being enclosed in a wrapper object. The reportUID and studyInstanceUID fields should be provided in the root of the notificationContent without any additional nesting
4. The URL must be a valid HTTPS URL. It follows the standard URL structure, including a protocol (https), a domain name (webhook.site), and a path like 3b8c180a-dcac-4700-afee-9ebde4abbfcb.