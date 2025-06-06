PGDMP                      }            vc-users    17.2    17.2     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            �           1262    55573    vc-users    DATABASE     �   CREATE DATABASE "vc-users" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Kazakh_Kazakhstan.utf8';
    DROP DATABASE "vc-users";
                     postgres    false            �            1259    55589    user_follows    TABLE     �   CREATE TABLE public.user_follows (
    follower_id integer NOT NULL,
    followed_id integer NOT NULL,
    followed_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_no_self_follow CHECK ((follower_id <> followed_id))
);
     DROP TABLE public.user_follows;
       public         heap r       postgres    false            �            1259    55576    users    TABLE     �  CREATE TABLE public.users (
    user_id integer NOT NULL,
    username character varying(50) NOT NULL,
    email character varying(255) NOT NULL,
    user_password character varying(255) NOT NULL,
    display_name character varying(100),
    bio text,
    avatar_url character varying(255),
    created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
    last_login timestamp with time zone,
    is_active boolean DEFAULT true
);
    DROP TABLE public.users;
       public         heap r       postgres    false            �            1259    55574    users_user_id_seq    SEQUENCE     �   CREATE SEQUENCE public.users_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.users_user_id_seq;
       public               postgres    false            �            1259    55575    users_user_id_seq1    SEQUENCE     �   ALTER TABLE public.users ALTER COLUMN user_id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.users_user_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public               postgres    false    219            �          0    55589    user_follows 
   TABLE DATA           M   COPY public.user_follows (follower_id, followed_id, followed_at) FROM stdin;
    public               postgres    false    220   �       �          0    55576    users 
   TABLE DATA           �   COPY public.users (user_id, username, email, user_password, display_name, bio, avatar_url, created_at, last_login, is_active) FROM stdin;
    public               postgres    false    219   �       �           0    0    users_user_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.users_user_id_seq', 1, false);
          public               postgres    false    217            �           0    0    users_user_id_seq1    SEQUENCE SET     @   SELECT pg_catalog.setval('public.users_user_id_seq1', 9, true);
          public               postgres    false    218            3           2606    55595    user_follows user_follows_pkey 
   CONSTRAINT     r   ALTER TABLE ONLY public.user_follows
    ADD CONSTRAINT user_follows_pkey PRIMARY KEY (follower_id, followed_id);
 H   ALTER TABLE ONLY public.user_follows DROP CONSTRAINT user_follows_pkey;
       public                 postgres    false    220    220            +           2606    55588    users users_email_key 
   CONSTRAINT     Q   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_key UNIQUE (email);
 ?   ALTER TABLE ONLY public.users DROP CONSTRAINT users_email_key;
       public                 postgres    false    219            -           2606    55584    users users_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public                 postgres    false    219            /           2606    55586    users users_username_key 
   CONSTRAINT     W   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_username_key UNIQUE (username);
 B   ALTER TABLE ONLY public.users DROP CONSTRAINT users_username_key;
       public                 postgres    false    219            0           1259    55607    idx_user_follows_followed_id    INDEX     \   CREATE INDEX idx_user_follows_followed_id ON public.user_follows USING btree (followed_id);
 0   DROP INDEX public.idx_user_follows_followed_id;
       public                 postgres    false    220            1           1259    55606    idx_user_follows_follower_id    INDEX     \   CREATE INDEX idx_user_follows_follower_id ON public.user_follows USING btree (follower_id);
 0   DROP INDEX public.idx_user_follows_follower_id;
       public                 postgres    false    220            4           2606    55601 *   user_follows user_follows_followed_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_follows
    ADD CONSTRAINT user_follows_followed_id_fkey FOREIGN KEY (followed_id) REFERENCES public.users(user_id) ON DELETE CASCADE;
 T   ALTER TABLE ONLY public.user_follows DROP CONSTRAINT user_follows_followed_id_fkey;
       public               postgres    false    220    219    4653            5           2606    55596 *   user_follows user_follows_follower_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_follows
    ADD CONSTRAINT user_follows_follower_id_fkey FOREIGN KEY (follower_id) REFERENCES public.users(user_id) ON DELETE CASCADE;
 T   ALTER TABLE ONLY public.user_follows DROP CONSTRAINT user_follows_follower_id_fkey;
       public               postgres    false    4653    219    220            �      x������ � �      �   �  x�]S�v�8]���"��z��J�i <B7a2$�7�D�����GvzrBα-����[�J؉������f"Up�3�<�ʴ���u&M��1;���B��zp"����$�s]��@�K0�7y$���=�9
�ZT�0���q7D�`t�.������<�~��OM�Y�m�#~��j�iw7�_Ϸr�'wr��+z{�'U��1�唲U�s�i���˩���<_��E�'9��H>��5,[|X��E�ֱB;	a�3"���(�Y1�M_��er�I�֠�{��W	
%���SIb�R�	D�{p��$�v��j)Aւ�VJ�M���M"sPH](	ΩR}R��6Vl�gf *ٓwx��0�$�퐫�"7D�Ѡ3�h̂�3��O9�P3�gY�쌴�������i�l&S��C�85qE�j}�����E;O�9��\������ߞR(����B��1����w����9t��(~�8M�k�ȝ����K�}��vK^;b��_�,܂��bRN]%����sR�Gc\'�����W
	�(���	�1߿�أ��@�����?��"2��ϗ�i�Srx�g'g����[��,s�{�P��\쀎J2E��c۳�������v�vc�:�|�B���^�*���0�~����d��������s�ׂpH��{����������?9dE�     